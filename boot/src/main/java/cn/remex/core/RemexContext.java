/**
 * 
 */
package cn.remex.core;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统上下文变量
 * @author Hengyang Liu
 * @since 2012-4-20
 * 
 */
public class RemexContext {

	public static final String REMEX_CONTEXT = "RMX";
	
	public static final String REMEX_TICKET="curTicket";

	private static RemexContext GlobalContext = new RemexContext();

	private static final String BEAN = "cn.remex.core.RemexContext.bean";
	private static final String CURUSER = "cn.remex.core.RemexContext.curUser";
	private static final String PARAMETERS = "cn.remex.core.RemexContext.parameters";

	public static final String REMEX_CONTEXT_RequestBody = "cn.remex.core.RemexContext.RequestBody";

	static ThreadLocal<RemexContext> remexContext = new ThreadLocal<RemexContext>();

	/**
	 * Returns the ActionContext specific to the current thread.
	 * 
	 * @return the ActionContext for the current thread, is never <tt>null</tt>.
	 */
	public static RemexContext getContext() {
		RemexContext ctx = remexContext.get();
		if (null == ctx) {
			ctx = new RemexContext();
			setContext(ctx);
		}
		return ctx;
	}

	public static RemexContext getGlobalContext() {
		return GlobalContext;
	}

	/**
	 * Sets the action context for the current thread.
	 * 
	 * @param context
	 *            the action context.
	 */
	public static void setContext(final RemexContext context) {
		remexContext.set(context);
	}

	/**
	 * Constant that indicates the action is running under a "development mode".
	 * This mode provides more feedback that is useful for developers but
	 * probably too verbose/error prone for production.
	 */
	// public static final String DEV_MODE = "__devMode";

	Map<String, Object> context;

	/**
	 * Creates a new ActionContext initialized with another context.
	 * 
	 * @param context
	 *            a context map.
	 */
	public RemexContext() {
		this.context = new HashMap<String, Object>();
	}

	/**
	 * Returns a value that is stored in the current ActionContext by doing a
	 * lookup using the value's key.
	 * 
	 * @param key
	 *            the key used to find the value.
	 * @return the value that was found using the key or <tt>null</tt> if the
	 *         key was not found.
	 */
	public Object get(final String key) {
		return this.context.get(key);
	}

	public Object getUser() {
		return get(CURUSER);
	}

	public Object getBean() {
		return get(BEAN);
	}
	/**
	 * 获取在RemexContext bean中配置的RequestBody的内容
	 * @return Object.toString  返回requestBody对象地址
	 */
	public String getRequestBody() {
		Object o = get(REMEX_CONTEXT_RequestBody);
		Assert.notNull(o,"获取RequestBody的内容需要在RemexContext bean中配置！");
		return o.toString();
	}


	/**
	 * Gets the context map.
	 * 
	 * @return the context map.
	 */
	public Map<String, Object> getContextMap() {
		return this.context;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters() {
		return (Map<String, Object>) get(PARAMETERS);
	}

	/**
	 * Stores a value in the current RemexContext. The value can be looked up
	 * using the key.
	 * 
	 * @param key
	 *            the key of the value.
	 * @param value
	 *            the value to be stored.
	 */
	public void put(final String key, final Object value) {
		this.context.put(key, value);
	}

	public void setBean(final Object bean) {
		put(BEAN, bean);
	}
	
	public void setUser(final Object user) {
		put(CURUSER, user);
	}

	

	// public <T> T getInstance(Class<T> type) {
	// Container cont = getContainer();
	// if (cont != null) {
	// return cont.getInstance(type);
	// } else {
	// throw new
	// XWorkException("Cannot find an initialized container for this request.");
	// }
	// }

	/**
	 * Sets the action's context map.
	 * 
	 * @param contextMap
	 *            the context map.
	 */
	public void setContextMap(final Map<String, Object> contextMap) {
		getContext().context = contextMap;
	}
	public void setParameters(final Map<String, ?> parameters) {
		put(PARAMETERS, parameters);
	}


}
