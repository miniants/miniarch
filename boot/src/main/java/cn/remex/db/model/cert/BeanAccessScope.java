/**
 * 
 */
package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;

/**
 * @author Hengyang Liu
 * @since 2012-5-17
 *
 */
public class BeanAccessScope extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5001204467233290572L;

	private String beanName;
	private String scope;
	/**
	 * @return the beanName
	 */
	@Element(label="数据模型bean名称")
	public String getBeanName() {
		return this.beanName;
	}
	/**
	 * @return the scope
	 */
	@Element(label="可被访问的范围",edittype=EditType.select,
			editoptions="{value:{'"+DataAccessScope.everyone+"':'"+DataAccessScope.everyone+"','"+DataAccessScope.roleControl+"':'"+DataAccessScope.roleControl+"'}}")
	public String getScope() {
		return this.scope;
	}
	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(final String beanName) {
		this.beanName = beanName;
	}
	/**
	 * @param scope the scope to set
	 */
	public void setScope(final String scope) {
		this.scope = scope;
	}

}
