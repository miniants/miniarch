/**
 * 
 */
package cn.remex.db.cert;

import cn.remex.core.util.DateHelper;
import cn.remex.db.DbCvo;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.SqlBean;

/**
 * @author Hengyang Liu
 * @since 2012-4-27
 * 
 */
final public class DataAccessConfiguration {

	private static DataAccessAuditor dataAccessAuditor = null;

	public static DataAccessAuditor getAccessAuditor() {
		return dataAccessAuditor;
	}

	public static void setAccessAuditor(DataAccessAuditor accessAuditor) {
		DataAccessConfiguration.dataAccessAuditor = accessAuditor;
	}

	public static <T extends Modelable> String assignAuditedSql(SqlBean<T> sqlBean) {
		if (null != dataAccessAuditor) {
			for (String name : dataAccessAuditor.obtainAuditedParams(sqlBean)) {
				sqlBean.addNamedParam(name, "");
			}
			return dataAccessAuditor.obtainAuditedSql(sqlBean);

		} else {
			return sqlBean.getSqlString();
		}
	}

	public static <T extends Modelable> void assignAuditedValues(DbCvo<T> cvo) {
		String oper = cvo.$V(RsqlConstants.PN_oper);

		// 一些处理工作
		// 转化参数名称。
		if (SqlOper.add.equals(oper)) {
			String n = DateHelper.getNow();
			cvo.$S(RsqlConstants.SYS_createOperator, "");
			cvo.$S(RsqlConstants.SYS_createOperator_name, "");
			cvo.$S(RsqlConstants.SYS_createTime, n);
			cvo.$S(RsqlConstants.SYS_ownership, "");
			cvo.$S(RsqlConstants.SYS_ownership_name, "");
			cvo.$S(RsqlConstants.SYS_modifyOperator, "");
			cvo.$S(RsqlConstants.SYS_modifyOperator_name,"");
			cvo.$S(RsqlConstants.SYS_modifyTime, n);
		} else if (SqlOper.edit.equals(oper)) {
			cvo.$S(RsqlConstants.SYS_modifyOperator, "");
			cvo.$S(RsqlConstants.SYS_modifyOperator_name, "");
			cvo.$S(RsqlConstants.SYS_modifyTime, DateHelper.getNow());
		}
	}
}
