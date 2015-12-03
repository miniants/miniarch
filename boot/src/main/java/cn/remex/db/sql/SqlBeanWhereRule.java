package cn.remex.db.sql;

import cn.remex.db.WhereRuleOper;

import java.io.Serializable;

public class SqlBeanWhereRule implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1487114736707926155L;

	private String data;	//选择的查询值

	private String field;  	//查询字段
	private String op;		//查询操作
	private String paramName; // 命名参数名称
	public SqlBeanWhereRule() {
		super();
	}
	public SqlBeanWhereRule(final String field, final WhereRuleOper ruleOper, final String data) {
		super();
		this.field = field;
		this.op = ruleOper.toString();
		this.data = data;
	}
	public String getData() {
		return this.data;
	}
	public String getField() {
		return this.field;
	}
	public String getOp() {
		return this.op;
	}
	public String getParamName() {
		return this.paramName;
	}
	public String obtainKey() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.field);
		builder.append("_");
		builder.append(this.op);
		return builder.toString();
	}
	public void setData(final String data) {
		this.data = data;
	}

	public void setField(final String field) {
		this.field = field;
	}
	public void setOp(final String op) {
		this.op = op;
	}
	public void setParamName(final String paramName) {
		this.paramName = paramName;
	}

}

