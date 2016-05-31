/**
 * 
 */
package cn.remex.db.rsql;

import cn.remex.core.exception.ServiceCode;
import cn.remex.db.DbCvo;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;

/**
 * @author Hengyang Liu
 * @since 2012-4-4
 *
 */
public abstract class RsqlAssert {
	public static <T extends Modelable> void isOkRsqlCvo(final DbCvo<T> dbCvo) {
		// 数据库条件准备检查 TODO
//		if (dbCvo != null && !dbCvo.check()) {
//			final SqlBean<T> sqlBean = dbCvo.getSqlBean();
//
//			// 清楚参数请获取现场
//			String ss = sqlBean.getSqlString();
//			String nps = sqlBean.getNamedParams().toString();
//
//			// 必须清除参数
//			dbCvo.clear();
//			throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "数据库操作参数不充分或不合法: sql=" + ss + ";nps=" + nps);
//		}

	}

	public static void isOrmBean(final Object obj) {
		// 没有在配置文件中配置的bean不保存
		if(!Modelable.class.isAssignableFrom(obj.getClass())){
			throw new RsqlExecuteException(ServiceCode.RSQL_BEANCLASS_ERROR, "数据库操作的对象不是继承于modelable");
		}
	}
	
	public static void isOrmClass(String spaceName, final Class<?> clazz) {
		// 没有在配置文件中配置的bean不保存
		if(!RDBManager.getLocalSpaceConfig(spaceName).hasOrmBeanClass(clazz)){
			throw new RsqlExecuteException(ServiceCode.RSQL_BEANCLASS_ERROR, "数据库操作的类型不是继承于modelable");
		}
	}

	public static void notNullBean(final Object object) {
		if (object == null) {
			throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "不能保存空对象");
		}
	}
}
