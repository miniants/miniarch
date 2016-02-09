package cn.remex.web;

import cn.remex.RemexConstants;
import cn.remex.admin.auth.AuthenticateBtx;
import cn.remex.core.CoreSvo;
import cn.remex.core.RemexApplication;
import cn.remex.core.exception.FilterException;
import cn.remex.core.exception.NestedException;
import cn.remex.core.util.RequestHelper;
import cn.remex.db.model.cert.AuthUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class RemexFilter implements Filter, RemexConstants {
    /**
     * Filter的配置
     */
    private FilterConfig filterConfig = null;
    /**
     * 配置是否需要验证身份
     */
    private boolean needAuthenticate = false;
    /**
     * 允许直接访问的资源,可以是目录\文件,以;隔离
     */
    private ArrayList<String> permitUriPres = new ArrayList<String>();

    /**
     * 当前网站的程序URI目录
     */
    private String urlRoot = null;
    /**
     * 异常处理页面
     */
    private String errorUri;
    /**
     * 需要登录时的页面
     */
    private String loginUri;

    @Override
    public void destroy() {
        //TODO 此处预留给需要在重启服务或者服务退出是清理的资源
    }

    /**
     * 这里能捕捉尚未转入框架的异常。
     */
    @Override
    public void doFilter(final ServletRequest sRequest, final ServletResponse sResponse, final FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) sRequest;
        HttpServletResponse response = (HttpServletResponse) sResponse;

        String uri = request.getRequestURI();
        String svlPath = request.getServletPath();
        String uriParams = request.getQueryString();
        String context = request.getContextPath();
        CoreSvo.initHttp(request, response);
        try {
            if (/* !needAuthenticate || */
                    isPublic(request) || // 如果需要验证, // 如果没有在不需要验证的目录中或不是公共文件
                    (AuthenticateBtx.checkToken() &&  isPermit(request))) {
//                //短链接/加密连接rewrite 此功能暂时用不上
//                if (RemexRewritUrl.isRemexWebEncodeUrl(svlPath))
//                    request.getRequestDispatcher(RemexRewritUrl.decodeUrl(svlPath)).forward(request, response);
//                else
                chain.doFilter(sRequest, sResponse);
            } else {
                // //如果验证没有通过
                logger.warn("权限验证没有通过，客户端IP:"+ RequestHelper.getClientIP(request)+",URI:"+uri);
                request.setAttribute("redirectURI", urlRoot + request.getServletPath() /*+ (Judgment.nullOrBlank(uriParams)?"":"?" + uriParams)*/);
                response.sendRedirect(context+"/static/framework/login.html");

            }
        } catch (Exception e) {
            // 这里能捕捉尚未转入struts2 中的异常。@l
            logger.error("服务进入应用框架之前发生异常:", e);
            request.setAttribute("exception", e);
             request.getRequestDispatcher(errorUri).forward(request, response);
        }
        CoreSvo.destoryHttp();
        timeLogger.info(new StringBuilder("Handle request [").append(uri).append("?").append(uriParams).append("] took ").append(System.currentTimeMillis() - startTime).append(" ms.")
                .append("\r\n==============================================================\r\n"));
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        /************ 数据库配置初始化 *************************************************/
        try {
            RemexApplication.refresh();
        } catch (NestedException e) {
            throw new FilterException("RemexFilter初始化错误，架构捕获到数据库异常:", e);
        } catch (Exception e) {
            throw new FilterException("RemexFilter初始化错误，架构未处理的数据库异常:", e);
        }

        /***************** 认证配置 **********************************/
        // 在web.xml中定义的参数，设置是否需要验证
        String needAuthenticateString = this.filterConfig.getInitParameter("needAuthenticate");
        // 在web.xml中定义的参数，设置需要验证的页面
        for (String dir : this.filterConfig.getInitParameter("permitUriPres").split(";"))
            permitUriPres.add(dir.trim());
        // 配置程序目录
        urlRoot = this.filterConfig.getServletContext().getContextPath();

        if ("true".equals(needAuthenticateString)) {
            System.out.println("系统配置为身份验证，需进行安全认证。");
        } else{
            System.out.println("系统配置为通行模式，无需验证即可访问。");
        }

    }

    private boolean isPublic(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        if(uri.equals("/")||uri.contains("/static")||uri.contains("/AdminBs/login"))
            return true;
        return false;
    }

    private boolean isPermit(final HttpServletRequest request) {
        if(true) return true;
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        //任意角色匹配即可访问
        uri = uri.replaceFirst("/smvc", "").replaceAll(".json","").replaceAll(".jsp","").replaceAll(ctx, "")
                .replaceAll("/[a-zA-Z\\-]*[\\d]+$","");
        if(uri.equals("/"))
            return true;
        Map<String, Map<String, ?>> uris = AuthenticateBtx.obtainSysUriMapToRole();
        Map<String, ?> rolesOfCurUri = uris.get(uri);
        if(null == rolesOfCurUri)
            return false;
        boolean permit = AuthenticateBtx.obtainCurUser().getRoles().stream().anyMatch(curRole ->
                        rolesOfCurUri.containsKey(curRole.getId())
        );
        return permit;
    }

}
