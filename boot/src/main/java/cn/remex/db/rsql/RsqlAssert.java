/**
 * 
 */
package cn.remex.db.rsql;

import cn.remex.db.DbCvo;
import cn.remex.db.exception.FatalOrmBeanException;
import cn.remex.db.exception.RsqlDBExecuteException;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.rsql.connection.RDBConnection;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.SqlBean;

/**
 * @author Hengyang Liu
 * @since 2012-4-4
 *
 */
public abstract class RsqlAssert {
	/**
	 * @param dbCvo
	 * @param con
	 * @rmx.call {@link RsqlDao#execute(DbCvo)}
	 * @rmx.call {@link RsqlDao#executeQuery(DbCvo)}
	 * @rmx.call {@link RsqlDao#executeUpdate(DbCvo)}
	 */
	public static <T extends Modelable> void isOkRDBConnection(final DbCvo<T> dbCvo, final RDBConnection con) {
		if (!con.isOK()) {
			// 必须清除参数
			dbCvo.clear();
			// 必须考虑返回数据库连接，由于此处数据连接无效，故无须返回。
			// con.free();
			throw new RsqlDBExecuteException("无法获取数据库连接！") ;
		}

	}
	/**
	 * @param dbCvo
	 * @rmx.call {@link RsqlDao#execute(DbCvo)}
	 * @rmx.call {@link RsqlDao#executeQuery(DbCvo)}
	 * @rmx.call {@link RsqlDao#executeUpdate(DbCvo)}
	 */
	public static <T extends Modelable> void isOkRsqlCvo(final DbCvo<T> dbCvo) {
		// 数据库条件准备检查
		if (dbCvo != null && !dbCvo.check()) {
			final SqlBean<T> sqlBean = dbCvo.getSqlBean();
			
			// 清楚参数请获取现场
			String ss = sqlBean.getSqlString();
			String nps = sqlBean.getNamedParams().toString();

			// 必须清除参数
			dbCvo.clear();
			throw new RsqlDBExecuteException(ss, nps, "数据库操作参数不充分或不合法!");
		}

	}
	
	
	/**
	 * @param obj
	 * @rmx.call {@link RsqlContainer#store(Modelable, DbCvo)}
	 */
	public static void isOrmBean(final Object obj) {
		// 没有在配置文件中配置的bean不保存
		if(!Modelable.class.isAssignableFrom(obj.getClass())){
			throw new FatalOrmBeanException(obj.getClass()+"不是继承于modelable,Rsql无法保存它。");
		}
	}
	
	/**
	 * @param clazz
	 */
	public static void isOrmClass(final Class<?> clazz) {
		// 没有在配置文件中配置的bean不保存
		if(RDBManager.getLocalSpaceConfig().hasOrmBeanClass(clazz)){
			throw new FatalOrmBeanException(clazz+"不是ORM类型！");
		}
	}
	
	/**
	 * @param beanName
	 */
	public static void isOrmBeanName(final String beanName) {
		// 没有在配置文件中配置的bean不保存
		if(null==RDBManager.getLocalSpaceConfig().getOrmBeanClass(beanName)){
			throw new FatalOrmBeanException(beanName+"不是ORM beanName!");
		}
	}

	/**
	 * @param object
	 * @rmx.call {@link RsqlContainer#store(Modelable, DbCvo)}
	 */
	public static void notNullBean(final Object object) {
		if (object == null) {
			throw new RsqlExecuteException("不能保存空对象");
		}
	}

	/**
	 * @param object
	 * @param msg
	 * @rmx.call {@link RsqlCore#createDBBean(Class)}
	 */
	public static void notNull(final Object object,final String msg) {
		if (object == null) {
			throw new RsqlExecuteException(msg);
		}
	}

}
