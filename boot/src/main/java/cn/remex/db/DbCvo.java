package cn.remex.db;

import cn.remex.core.util.Assert;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.Sort;
import cn.remex.db.sql.SqlBean;
import cn.remex.db.sql.SqlType;
import cn.remex.db.lambdaapi.WherePredicate;
import cn.remex.db.sql.WhereRuleOper;

import java.lang.reflect.Type;
import java.util.Map;

public class DbCvo<T extends Modelable> extends DbCvoChain<T> {

	/**
	 * 用于通过切面捕获lambda表达式中调用get方法而获得field的.
	 */
	protected T aopBean;
	public T obtainAOPBean(){
		return aopBean!=null? aopBean:(aopBean = createAOPBean(beanClass));
	}

	public void initParam() {
		this.isInit = true;

		//LHY 2015-2-17 此处可添加在数据库操作之前必须检查的逻辑
		if(null!=this.dataColumns && null!=beanClass){
			Map<String, Type> allfields = SqlType.getFields(beanClass, SqlType.FieldType.TAll);
			for(String field:this.dataColumns.split(";")){
				if(!allfields.containsKey(field))
					throw new RsqlExecuteException("执行数据操作时发现，数据库模型"+beanName+"中不存在属性"+field);
			}
		}

		this.sqlBean = SqlBean.getInstance(this);
		this.sqlBean.initParam(this);
	}
	/**
	 * 通过beanClass 来初始化一个DbCvo，默认的操作为list，数据类型为bd
	 * 
	 * @param beanClass dbCvo内部处理了关于cglib代理导致的问题。
	 */
	public DbCvo(Class<T> beanClass) {
		init(beanClass);
	}
	public DbCvo(final Class<T> beanClass, final Map<String, Object> params) {
		if (null != params)
			putParameters(params);
		
		init(beanClass);
	}
	public DbCvo(Class<T> beanClass,SqlOper oper) {
		Assert.notNull(oper,"数据库操作符oper不能为空！");
		this.oper = oper;
		init(beanClass);
	}
	public DbCvo(Class<T> beanClass,SqlOper oper,String dataType) {
		Assert.notNull(oper,"数据库操作符oper不能为空！");
		this.oper = oper;
		if(null!=dataType)this.dataType = dataType;
		init(beanClass);
	}
	public DbCvo(Class<T> beanClass,SqlOper oper,String dataType,String dataColumns){
		Assert.notNull(oper,"数据库操作符oper不能为空！");
		this.oper = oper;
		if(null!=dataType)this.dataType = dataType;
		if(null!=dataColumns)this.dataColumns = dataColumns;
		init(beanClass);
	}
	/**
	 * 此构造用于执行sql语句，如果需要参数，则sqlString中可以用:paramName。
	 * 相对应params中的key必须为相应的paramName
	 * 
	 * @param sqlString
	 * @param params
	 */
	public DbCvo(final String sqlString, final Map<String, Object> params) {
		this.sqlString = sqlString;
		this.oper = SqlOper.sql;
		if (null != params)
			putParameters(params);
		init(null);
	}
	/**
	 * 此构造用于执行sql语句，如果需要参数，则sqlString中可以用:paramName。
	 * 相对应params中的key必须为相应的paramName
	 * 
	 * @param sqlString
	 * @param params
	 */
	public DbCvo(final String sqlString, final SqlOper oper, final Map<String, Object> params) {
		this.sqlString = sqlString;
		this.oper = oper;
		if (null != params)
			putParameters(params);
		init(null);
	}
	public DbCvo<T> dataColumns(WherePredicate<T>... ws){
        ws[0].init(obtainAOPBean());
		return this;
	}
	public DbCvo<T> filterBy(WherePredicate<T> wp, WhereRuleOper oper, String value) {
        wp.init(obtainAOPBean());
        String fieldName = obtainPredicateBeanField(null);
        this.putRule(fieldName, oper, value);
        return this;
    }
    public DbCvo<T> orderBy(WherePredicate<T> wp,Sort s) {
        wp.init(obtainAOPBean());
        String fieldName = obtainPredicateBeanField(null);
        this.putOrder(true,fieldName,s.toString());
        return this;
    }
}
