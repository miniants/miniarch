package zbh.wx.cert;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import zbh.remex.cert.AuthenticateBean;
import zbh.remex.cert.CertConfiguration;
import cn.remex.core.RemexContext;
import cn.remex.db.model.cert.AuthUser;
import zbh.remex.util.RequestHelper;

public class AuthenticateBtx {
	/** 用户session对象 */
	public static AuthUser sessionUser = null;
	// public static AuthUser getSessionUser() {
	// AuthenticateBean ab = (AuthenticateBean) CoreSvo.$VS(sessionName);
	// return null == ab?null:ab.getUser();
	// }
	/** session 用户名属性名 */
	public final static String sessionName = "AncarAuthenticateBean";
	/** session 在线用户名属性名 */
	public final static String onlineUserName = "AncarOnlineUserName";
	/** session 为确定用户时的系统访客属性名 */
	public final static String guestUserName = "AncarGuestUserName";
	/** 存放系统相关的用户等session变量的map */
	public final static Map<String, Object> sMap = new HashMap<String, Object>();

	private boolean needLogMsg = false;

	/**
	 * 本接口用于进行用户身份认证及权限检查的转发类入口
	 * 
	 * @rmx.summary 主要用于将数据根据逻辑转发至
	 *              {@link AuthenticateBean#authenticate(String, Map)}和
	 *              {@link AuthenticateBean#validate(String, String, Map)}中<br>
	 *              filter将验证信息发送至该处，查询session回话中有无会话信息，无则先将参数中的用户相关信息发送至
	 *              {@link AuthenticateBean#authenticate(String, Map)}中校验用户权限，
	 *              有则表示用户信息校验成功，登录成功，继续将请求-url、param、value发送至
	 *              {@link AuthenticateBean#validate(String, String, Map)}中校验请求的权限<br>
	 * @param request
	 * @return true代表
	 * @throws Exception
	 * @rmx.call {@link zbh.remex.RemexFilter# doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain chain)}
	 */
	public boolean authenticate(final HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String servletPath = request.getServletPath();
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		Map<?, ?> parameterMap = request.getParameterMap();
		String clientIP = RequestHelper.getClientIP(request);
//		String usernames[] = (String[]) parameterMap.get("username");
//		String passwords[] = (String[]) parameterMap.get("password");

		return authenticate(requestURI, servletPath, contextPath, clientIP, parameterMap, session);
	}

	public boolean authenticate(String requestURI, String servletPath, String contextPath, String clientIP, Map<?, ?> parameterMap, HttpSession session) throws Exception {
		boolean b = true;

		AuthenticateBean authenticateBean = (AuthenticateBean) session.getAttribute(sessionName);
		// 存放系统相关的用户等session变量的map
		RemexContext.getContext().setContextMap(sMap);
		// 必须初始化当前用户
		if (null == authenticateBean || null == authenticateBean.getUser()) {
			sMap.put(guestUserName, CertConfiguration.GUEST);
		}

		// ....验证身份，第一次访问详细验证权限
		// 确保authenticateBean有效，凡是无效的验证数据不保存且返回验证失败
		if (authenticateBean == null) {
			// 进行用户定义的验证
			authenticateBean = new AuthenticateBean();
			RemexContext.getContext().setBean(authenticateBean);
			// 如果验证成功则保存Bean于Session中
			// 如果验证失败必须清空该session
//			if (authenticateBean.authenticate(requestURI, username, password)) {
//				session.setAttribute(sessionName, authenticateBean);
//				sessionUser = authenticateBean.getUser();
//				if (this.needLogMsg) {
//					Container cs = ContainerFactory.getSession();
//					// cs.beginTransaction();
//					cs.store(new LogonLogMsg("登录成功", "登录成功...", DateHelper.getNow(), authenticateBean.getUsername(), clientIP, servletPath));
//					// cs.finishTransaction();
//				}
//				sMap.put(onlineUserName, CertConfiguration.GUEST);
//				// RemexContext.getContext().setContextMap(sMap);
//				b = true;
//			} else {
				// localAuthenticateBean.set(null);
//				session.setAttribute(sessionName, null);
//				sMap.remove(onlineUserName);
//				// RemexContext.removeOnlineUser(authenticateBean.getUser());
//				if (this.needLogMsg && username != null) {// 如果不是直接访问的登录失败属于登录尝试。
//					Container cs = ContainerFactory.getSession();
//					cs.store(new LogonLogMsg("登录失败！", authenticateBean.getUsername() + "@" + authenticateBean.getPassword() + "登录失败！", DateHelper.getNow(), authenticateBean.getUsername(), clientIP, servletPath));
//				}
//				b = false;
//			}
			RemexContext.getContext().setContextMap(sMap);
		}
		// localAuthenticateBean.set(authenticateBean);
		RemexContext.getContext().setBean(authenticateBean.getUser());
		return b && authenticateBean.validate(contextPath, servletPath, parameterMap);
	}

	/**
	 * 根据在web.xml中配置参数判断是否需要记录日志
	 * 
	 * @param needLogMsg
	 *          是否需要记录日志的标志
	 * @rmx.call {@link zbh.remex.RemexFilter#init(FilterConfig filterConfig)}
	 */
	public void setNeedLogMsg(final String needLogMsg) {
		if ("true".equals(needLogMsg)) {
			this.needLogMsg = true;
		} else {
			this.needLogMsg = false;
		}
	}

	// private boolean reqAuthenticate(final HttpServletRequest request){
	// return false;
	// }
	//
	// private boolean isUnimpededURI(){
	// return true;
	// }
}
