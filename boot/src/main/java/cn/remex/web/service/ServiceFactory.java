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
import cn.remex.core.RemexApplication;
import cn.remex.core.RemexRefreshable;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public final class ServiceFactory implements RemexRefreshable {
    public static Map<String, Class<?>> getBsMap() {
        return bsMap;
    }

    public static void setBsMap(Map<String, Class<?>> bsMap) {
        ServiceFactory.bsMap = bsMap;
    }

    private static Map<String, Class<?>> bsMap = new HashMap<String, Class<?>>();
    private static List<String> bsPackages = new ArrayList<String>();

    static {
        String defaultPackage = "cn.remex";
        bsPackages.add(defaultPackage);
        scanPackagesForBs(defaultPackage);
    }

    public void setBsPackages(List<String> curBsPackages) {
        curBsPackages.stream()
                .filter(p -> !bsPackages.contains(p))
                .forEach(p -> {
                    scanPackagesForBs(p);
                    bsPackages.add(p);
                });
    }

    /**
     * 扫描 cn.remex包下的bs 并初始化bs 根据类简称扫描是否有重复的bsName
     */
    private static void scanPackagesForBs(String bsPackage) {
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
    static public BsRvo executeBs(String bs, String bsCmd, String pk, HttpServletRequest request, HttpServletResponse response) {
        try {

            RemexConstants.logger.info("Executing Bs=" + bs + ";bsCmd=" + bsCmd);
            BusinessService bsan = ReflectUtil.getMethodAnnotation(bsMap.get(bs), bsCmd, BusinessService.class);
            Assert.notNull(bsan, "业务服务不存在!", BsException.class);

            Object bsObj = ServiceFactory.createBs(bs);
            Method cglibBsCmdMethod = ReflectUtil.getMethod(bsObj.getClass(), bsCmd); // 此处应该用实例的类来查找对应的方法,否则会越过代理直接调用原始方法

            ArrayList<Object> paramArra = new ArrayList();
            //处理文件上传
            if (null != bsan && bsan.withMultiPart()) {

            }
            Map params = new HashMap();
            params.putAll(request.getParameterMap());
            params.put("pk",pk);
            Parameter[] parameters = cglibBsCmdMethod.getParameters();
            for (Parameter param : parameters) {
                Class paramType = param.getType();
                String paramName = param.getName();
                Object paramObj;
                if (ReflectUtil.isSimpleType(paramType)) {
                    if (Judgment.nullOrBlank(paramName)) {
                        RemexConstants.logger.warn("无法获得参数名,本框架需要Java8以上版本,且编译时需要指定参数-parameters");
                    }
                    paramObj = ReflectUtil.caseObject(paramType, params.get(paramName));
                } else {
                    paramObj = ReflectUtil.invokeNewInstance(param.getType());
                    MapHelper.objectFromFlat(paramObj, params);
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

            return (BsRvo) ReflectUtil.applyMethod(cglibBsCmdMethod, bsObj, paramArra.toArray());
        } catch (Exception e) {
            RemexConstants.logger.error("调用本地Bs服务出现异常。", e);
            return new BsRvo(false, e.toString());
        }

    }

    @Override
    public void refresh() {
        bsMap = new HashMap<String, Class<?>>();
        bsPackages.stream().forEach(p -> {
            scanPackagesForBs(p);
        });
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
