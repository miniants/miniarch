/* 
 * 文 件 名 : BsFactory.java
 * CopyRright (c) since 2013: 
 * 文件编号： 
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2013-7-20
 * 修 改 人： 
 * 日   期： 
 * 描   述： 
 * 版 本 号： 1.0
 */

package cn.remex.web.service;

import cn.remex.RemexConstants;
import cn.remex.contrib.appbeans.DataRvo;
import cn.remex.core.RemexApplication;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.exception.NestedException;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.*;
import cn.remex.db.DbRvo;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.util.*;

/**
 * 业务分发处理 服务模型创建实例化,bs包结构扫描,bs模型示例创建初始化
 *
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2013-7-20
 */
public final class ServiceFactory{
    public static Map<String, Class<?>> getBsMap() {
        return bsMap;
    }
    private static Map<String, Class<?>> bsMap = new HashMap<>();

    /**
     * 扫描 cn.remex包下的bs 并初始化bs 根据类简称扫描是否有重复的bsName
     */
    public static void scanPackagesForBs(String bsPackage) {
        RemexConstants.logger.info("扫描BusinessPackage:" + bsPackage);
        Set<Class<?>> orbs = PackageUtil.getClasses(bsPackage);
        for (Class<?> c : orbs) {
            if (c.isAnnotationPresent(BusinessService.class) && !c.isInterface()) {
                String bsName = StringHelper.getClassSimpleName(c);
                if (bsMap.containsKey(bsName))
                    throw new BsException("扫描BusinessPackage发现异常,有重复的bsName:" + bsName);
                bsMap.put(bsName, (Class<?>) c);
            }
        }
    }

    static public Object createBs(String bsName) {
        Class<?> bs = bsMap.get(bsName);
        if (null == bs)
            throw new BsException("业务服务" + bsName + "未开发或者被管理员摒弃，请联系站点管理员！");

        try {
            return RemexApplication.getBean(bs);
        } catch (Exception e) {
            throw new BsException(bsName, e);
        }
    }

    /**
     * 根据bsName获得业务分发处理类
     */
    static public BsRvo executeBs(String bs, String bsCmd, String pk, HttpServletRequest request, HttpServletResponse response, Map<String,Object> params) {
        BsRvo bsRvo;
        String contentType = request.getContentType();
        boolean isText = Judgment.nullOrBlank(contentType) || contentType.contains("text/html")|| contentType.contains("text/xml");
        boolean isJson = !isText && Judgment.notEmpty(contentType) && contentType.contains("application/json");

        try {

            RemexConstants.logger.info("Executing Bs=" + bs + ";bsCmd=" + bsCmd);
            Assert.notNull(bsMap.get(bs), ServiceCode.BS_ERROR, "服务无效");
            BusinessService bsan = ReflectUtil.getMethodAnnotation(bsMap.get(bs), bsCmd, BusinessService.class);
            Assert.notNull(bsan, ServiceCode.BS_ERROR, "服务方法没有注解BusinessService，业务服务无效");

            Object bsObj = ServiceFactory.createBs(bs);
            Method cglibBsCmdMethod = ReflectUtil.getMethod(bsObj.getClass(), bsCmd); // 此处应该用实例的类来查找对应的方法,否则会越过代理直接调用原始方法
            Method CmdMethod = ReflectUtil.getMethod(bsMap.get(bs), bsCmd); // 此处应该用原始方法获取参数，否则参数名将在代理时丢失
            Parameter[] parameters = CmdMethod.getParameters();// 此处应该用原始方法获取参数，否则参数名将在代理时丢失

            //1.获取request 请求过来的参数列表
            params = params == null ? new HashMap() : params;
            params.putAll(request.getParameterMap());
            //处理文件上传
            if (bsan.withMultiPart() //主动声明有上传文件
                    || Arrays.asList(parameters).stream().filter(parameter -> File.class.isAssignableFrom(parameter.getType())).findAny().isPresent() //如果参数中有File
                    ) {
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //记录上传过程起始时的时间，用来计算上传时间
                    int pre = (int) System.currentTimeMillis();
                    //取得上传文件
                    String filename = iter.next();
                    MultipartFile file = multiRequest.getFile(filename);
                    if (file != null) {
                        params.put(filename, file);
                    }
                    RemexConstants.logger.info("处理multipart file="+filename+";size="+file.getSize()+";time="+(System.currentTimeMillis()-pre));
                }
            }
            String requestBody = null;
            //如果是json或者报文模式 TODO 此处仅支持json格式
            if (isJson || bsan.requestBody()) {
                requestBody = HttpHelper.obtainHttpPack(request.getInputStream());
                if (!Judgment.nullOrBlank(requestBody)) {
                    params.putAll(JsonHelper.toJavaObject(requestBody, HashMap.class));//将json字符串中根节点下的属性换成Map<String field,String strVal)
                }
            }
            //如果参数中需要将请求的报文放到一个变量中
            if (!Judgment.nullOrBlank(bsan.bodyParamName())) {
                requestBody = null!=requestBody?requestBody:HttpHelper.obtainHttpPack(request.getInputStream());
                params.put(bsan.bodyParamName(), requestBody);
            }
            params.put("pk", Judgment.nullOrBlank(pk) ? params.get("id") : pk);
            //end for 1

            //2.从request 过来的参数中提取所需要的 函数参数值列表
            ArrayList paramArra = new ArrayList(); // 调用函数的参数列表
            for (Parameter param : parameters) {
                Class paramType = param.getType();
                String paramName = param.getName();
                Object paramObj;
                if (ReflectUtil.isSimpleType(paramType)) {//简单类型直接赋值
                    paramObj = ReflectUtil.caseObject(paramType, params.get(paramName));
                    if(null==paramObj && ReflectUtil.isNumeralType(paramType))paramObj=0;//为空的数字类型，转换为0
                } else {//复杂类型，要么json转化，要么map赋值
                    if (isJson || bsan.requestBody()) { //如果是json或者指定requestBOdy参数来从http报文中解析参数对象，则通过JSON转化
                        paramObj = JsonHelper.toJavaObject(requestBody, param.getParameterizedType());
                    } else {//将params列表中的值一个个填进去 TODO 这里面遗留的问题：如不是json必须通过form key-value的参数提供参数值时，参数为复杂类型且其属性中也有复杂类型时，赋值可能存在丢失
                        paramObj = ReflectUtil.invokeNewInstance(paramType);
                        MapHelper.objectFromFlat(paramObj, params);//jqgrid 只能传k-v，用的事此方法
                    }
                }

                if (BsCvo.class.isAssignableFrom(paramType)) {//如果是符合框架bsCvo的模式,将参数列表放进去以便后续灵活的操作
                    BsCvo bsCvo = (BsCvo) paramObj;
                    if (bsCvo.getParams() != null) {
                        bsCvo.getParams().putAll(params);
                    } else {
                        bsCvo.setParams(params);
                    }
                    bsCvo.setContextPath(request.getContextPath());
                }

                paramArra.add(paramObj);
            }
            //end for 2

            //3.执行bs
            Object result = ReflectUtil.applyMethod(cglibBsCmdMethod, bsObj, paramArra.toArray());

            //4.对不同类型的结果予以适配
            if (result instanceof BsRvo) {
                bsRvo = (BsRvo) result;
            } else if (result instanceof DbRvo) {
                bsRvo = new DataRvo((DbRvo) result);
            } else if (null == result) {
                bsRvo = new BsRvo(ServiceCode.SUCCESS, "OK", "NO_RESPONSE_BODY");
            } else {
                bsRvo = new BsRvo(ServiceCode.SUCCESS, "OK", result);
            }
        } catch (NestedException e) {
            if(Judgment.nullOrBlank(e.getErrorCode()) || ServiceCode.ERROR.equals(e.getErrorCode())){
                RemexConstants.logger.error("服务失败, code="+e.getErrorCode()+";msg="+e.getMessage(),e);
            }else if(ServiceCode.ACCOUNT_NOT_AUTH.equals(e.getErrorCode()) || ServiceCode.BS_ERROR.equals(e.getErrorCode())){//未登录的异常,服务错误的异常，不用输出日志
                RemexConstants.logger.warn("服务失败, code="+e.getErrorCode()+";msg="+e.getMessage());
            }else{
                RemexConstants.logger.warn("服务失败, code=" + e.getErrorCode() + ";msg=" + e.getMessage(), e);
            }
            bsRvo = new BsRvo(e.getErrorCode(), e.getMessage());
            bsRvo.setRt("json");
            bsRvo.setRv("json");
        } catch (Throwable t) {
            RemexConstants.logger.error("调用本地Bs服务出现异常。", t);
            bsRvo = new BsRvo(ServiceCode.FAIL, t.getMessage());
            bsRvo.setRt("json");
            bsRvo.setRv("json");
        }

        if(isText && Judgment.nullOrBlank(bsRvo.getRv())){//直接输出body里面的文本
            bsRvo.setRv("text_layout");
        }

        return bsRvo;
    }


    private static Files[] uploadFiles(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //记录上传过程起始时的时间，用来计算上传时间
                int pre = (int) System.currentTimeMillis();
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        System.out.println(myFileName);
//                        //重命名上传后的文件名
//                        String fileName = "demoUpload" + file.getOriginalFilename();
//                        //定义上传路径
//                        String path = "H:/" + fileName;
//                        File localFile = new File(path);
//                        file.transferTo(localFile);
                    }
                }
                //记录上传该文件后的时间
            }

        }
        return null;
    }
}
