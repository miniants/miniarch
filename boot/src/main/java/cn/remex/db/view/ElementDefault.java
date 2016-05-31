/**
 * 
 */
package cn.remex.db.view;


/**
 * @author Hengyang Liu
 * @since 2012-5-1
 *
 */
public class ElementDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2903713561674676980L;

	private String defaultElement;

	@Element(label=ViewUtil.DefaultLabel)
	public String getDefaultElement() {
		return this.defaultElement;
	}

	public void setDefaultElement(final String defaultElement) {
		this.defaultElement = defaultElement;
	}
}
