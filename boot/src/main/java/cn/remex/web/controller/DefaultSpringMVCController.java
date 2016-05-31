package cn.remex.web.controller;

import cn.remex.RemexConstants;
import cn.remex.core.exception.NestedException;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.MapHelper;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.ServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping(value = "/hello")//表示要访问这个action的时候都要加上这个/hello路径
public class DefaultSpringMVCController implements RemexConstants {

    /* 接收参数getParameter()的时候:
     * 如果地址栏/springmvc/hello.htm上面没有传递参数,那么当id为int型的时候会报错,当id为Integer的时候值为null
     * 当地址栏为/springmvc/hello.htm?id=10的时候,action中有三种接收方式
     * 1、String hello(@RequestParam(value = "userid") int id),这样会把地址栏参数名为userid的值赋给参数id,如果用地址栏上的参数名为id,则接收不到
     * 2、String hello(@RequestParam int id),这种情况下默认会把id作为参数名来进行接收赋值
     * 3、String hello(int id),这种情况下也会默认把id作为参数名来进行接收赋值
        * 注:如果参数前面加上@RequestParam注解,如果地址栏上面没有加上该注解的参数,例如:id,那么会报404错误,找不到该路径
     */
    @RequestMapping(value = {"{bs:[a-zA-Z0-9]+}"})
    public ModelAndView execute1(@PathVariable String bs,
                                 @RequestParam(value = "files", required = false) CommonsMultipartFile[] files,
                                 HttpServletRequest request, HttpServletResponse response) {
        return execute(bs, "execute", null, request, response, files,null);
    }

    @RequestMapping(value = {"{bs:[a-zA-Z0-9]+}/{bsCmd:[a-zA-Z0-9]+}"})
    public ModelAndView execute2(@PathVariable String bs,
                                 @PathVariable String bsCmd,
                                 @RequestParam(value = "files", required = false) CommonsMultipartFile[] files,
                                 HttpServletRequest request, HttpServletResponse response) {
        return execute(bs, bsCmd, null, request, response, files,null);
    }
    @RequestMapping(value = {"DataService/{modelName:\\S+}/{bsCmd:[a-zA-Z0-9]+}"})
    public ModelAndView execute_DataService(
            @PathVariable String bsCmd,
            @PathVariable String modelName,
            @RequestParam(value = "files", required = false) CommonsMultipartFile[] files,
            HttpServletRequest request, HttpServletResponse response) {
            Map<String,Object> params = new HashMap<>();
            params.put("modelName",modelName);
        return execute("DataService", bsCmd,null, request, response, files,params);
    }
    @RequestMapping(value = {"ViewService/{viewName:\\S+}/{bsCmd:[a-zA-Z0-9]+}"})
    public ModelAndView execute_ViewService(
            @PathVariable String bsCmd,
            @PathVariable String modelName,
            HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> params = new HashMap<>();
        params.put("viewName",modelName);
        return execute("ViewService", bsCmd,null, request, response, null,params);
    }
    @RequestMapping(value = {"{bs:[a-zA-Z0-9]+}/{bsCmd:[a-zA-Z0-9]+}/{id:\\S+}"})
    public ModelAndView execute3(
            @PathVariable String bs, @PathVariable String bsCmd,
            @PathVariable String id,
            @RequestParam(value = "files", required = false) CommonsMultipartFile[] files,
            HttpServletRequest request, HttpServletResponse response) {
        return execute(bs, bsCmd, id, request, response, files,null);
    }


    @SuppressWarnings({"unchecked", "deprecation"})
    private ModelAndView execute(
            @PathVariable String bs, @PathVariable String bsCmd,
            @PathVariable String pk,
            HttpServletRequest request, HttpServletResponse response, CommonsMultipartFile[] files,Map params) {

        BsRvo bsRvo = ServiceFactory.executeBs(bs, bsCmd, pk, request, response, params);//调用service

        Map<String,Object> map = MapHelper.toMap(bsRvo);

        Object rt = request.getParameter("rt");//返回视图的类型,前端请求jsp json xml,重新定向或者指定特定的类型
        if (Judgment.nullOrBlank(rt) && Judgment.nullOrBlank(rt = map.get("rt"))) {
            if(Judgment.nullOrBlank(rt = bsRvo.getRt()))
                rt = "json";
        }
        Object rv = request.getParameter("rv");//返回视图的参数值,默认为/WEB-INF/page/bs_bsCmd.jsp
        if (Judgment.nullOrBlank(rv) && Judgment.nullOrBlank(rv = map.get("rv"))) {
            if(Judgment.nullOrBlank(rv = bsRvo.getRv()))
                rv = "default_layout";
        }
        Object rp = request.getParameter("rp");//返回视图的参数值,默认为/WEB-INF/page/bs_bsCmd.jsp
        if (Judgment.nullOrBlank(rp) && Judgment.nullOrBlank(rp = map.get("rp"))) {
            if(Judgment.nullOrBlank(rp = bsRvo.getRv()))
                map.put("rp",("bspage/" + bs + "_" + bsCmd).toLowerCase());
        }

        if(ServiceCode.ACCOUNT_NOT_AUTH.equals(bsRvo.getCode()) || ServiceCode.ACCOUNT_NOT_PERMIT.equals(bsRvo.getCode())){
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, bsRvo.getMsg());
            } catch (IOException e) {
                throw new NestedException(ServiceCode.ERROR, "账户401错误定向错误");
            }
        }

        if ("redirect".equals(rt)){
            return new ModelAndView("redirect:"+rv);
        }else{
            ModelAndView mv = new ModelAndView(rv.toString());
            map.forEach(mv::addObject);
            mv.addObject("pk", Judgment.nullOrBlank(pk)?map.get("pk"):pk);
            return mv;//不能重定向web-info里面的文件,而且需要写上绝对路径
        }


    }
}
