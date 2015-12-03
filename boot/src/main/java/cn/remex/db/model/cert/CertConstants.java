package cn.remex.db.model.cert;

/**
 * @since 20131105
 * @author zhangaiguo
 * 访问权限常量类
 */
public interface CertConstants {
	
	/**
	 *验证标识信息
	 */
	public enum VerifyFlag{
		/**允许访问*/
		permit,
		/**不允许访问*/
		forbidden,
		/**允许访问*/
		uncertainty
	}
	
	public enum VerifyOper{
		/**等于*/
		equals,
		/**包含*/
		contains,
	}
}
