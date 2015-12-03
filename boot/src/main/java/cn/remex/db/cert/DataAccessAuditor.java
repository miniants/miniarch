/**
 * 
 */
package cn.remex.db.cert;

import cn.remex.db.DbCvo;
import cn.remex.db.sql.SqlBean;

import java.util.List;




/**
 * @author Hengyang Liu
 * @since 2012-4-27
 *
 */
public interface DataAccessAuditor {
	
	/**
	 *  必须指定访问数据的相关参数。
	 * @param sqlBean
	 */

	public  List<String> obtainAuditedParams(SqlBean sqlBean);

	public void obtainAuditedValues(DbCvo cvo);

	public  String obtainAuditedSql(SqlBean sqlBean);
}
