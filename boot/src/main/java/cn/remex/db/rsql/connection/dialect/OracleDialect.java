/*
 * 文 件 名 : OracleDialect.java
 * CopyRright (c) since 2013:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2013-2-25
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */

package cn.remex.db.rsql.connection.dialect;

import cn.remex.core.exception.ServiceCode;
import cn.remex.db.exception.RsqlDialectException;

import java.sql.Types;
import java.util.Map;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-2-25
 *
 */
public class OracleDialect extends Dialect {
	@Override
	public String aliasFullName(final String tableAliasName, final String fieldName,final String aliasName){
		return new StringBuilder().append(tableAliasName)
				.append(".").append(openQuote()).append(fieldName).append(closeQuote())
				.append(" ").append(openQuote()).append(aliasName).append(closeQuote()).toString();

	}
	@Override
	public String aliasAggrFun(final String fieldName, final String aliasName){
		return new StringBuilder().append(fieldName)
				.append(" ").append(openQuote()).append(aliasName).append(closeQuote()).toString();

	}
	@Override
	public String obtainSelectRegex() {
		return "FROM\\s+\\"+openQuote()+"([\\w_0-9]+)\\"+closeQuote()+"\\s+[\\"+openQuote()+"]?([\\w_0-9]+)[\\"+closeQuote()+"]?";
	}

	@Override
	public String aliasTableName(final String tableName, final String aliasName) {
		return new StringBuilder().append(openQuote()).append(tableName).append(closeQuote())
				.append(" ").append(aliasName).toString();

	}

	@Override
	public char closeQuote() {
		return '"';
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#closeStringQuote()
	 */
	@Override
	public char closeStringQuote() {
		return '\'';
	}

	@Override
	public String concat(String...strings) {
		StringBuffer sb = new StringBuffer();
		int c=strings.length-1;
		for(int i=0;i<c;i++){
			sb.append(strings[i]).append("||");
		}
		return sb.append(strings[c]).toString();
	}

	@Override
	public String obtainPagingSQL(String sqlString,long start,long end,long rowCount) {
		StringBuilder sb = new StringBuilder("SELECT * FROM (SELECT A.*,ROWNUM rn FROM ( ")
		.append(sqlString)
		.append(") A WHERE ROWNUM<=").append(end).append(") WHERE rn>").append(start);
		return sb.toString();
	}

	@Override
	public String obtainSQLSelectIndexs(String beanName) {
		return "SELECT * FROM User_Indexes";
	}
	
	
	@Override
	public String obtainSQLIndexNameField() {
		return "INDEX_NAME";
	}

	@Override
	public String obtainSQLSelectTableNames() {
		return "SELECT TABLE_NAME AS NAME FROM "+quoteKey("USER_TABLES");
	}

	@Override
	public String obtainSQLSelectTablesColumnNames(final String tableName) {
		StringBuilder sqlString = new StringBuilder();
		sqlString.append("select t.COLUMN_NAME,DATA_TYPE,DATA_LENGTH,c.COMMENTS " +
				"from user_tab_columns t,user_col_comments c " +
				"where t.table_name = c.table_name and t.column_name = c.column_name " +
				"and t.table_name = :tableName");
		return sqlString.toString();
	}

	@Override
	public String obtainSQLTypeString(final int type) {
		return obtainSQLTypeString(type, -1);
	}

	@Override
	public String obtainSQLTypeString(final int type, final int length) {
		switch (type) {
		case Types.INTEGER:
		case Types.DOUBLE:
		case Types.FLOAT:
			return " NUMBER ";
		case Types.BOOLEAN:
			return " VARCHAR2(5) ";
		case Types.BIT:
			return " BIT ";
		case Types.CHAR:
			return " VARCHAR2("+(length==-1?600:length)+") ";
		case Types.DATE:
			return " VARCHAR2(20) ";
		case Types.JAVA_OBJECT:
			return " VARCHAR2(2000) ";
		case Types.CLOB:
			return " CLOB ";

		default:
			throw new RsqlDialectException(ServiceCode.RSQL_DIALECT_ERROR, OracleDialect.class + "中使用了未定义的SQLTypes!");
			//			return null;// never arrive here
		}
	}

	@Override
	public char openQuote() {
		// TODO Auto-generated method stub
		return '"';
	}



	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#openStringQuote()
	 */
	@Override
	public char openStringQuote() {
		return '\'';
	}



	@Override
	public String quoteAsString(final Object value) {
		if(null==value)
			return new StringBuilder().append("null").toString();
		return new StringBuilder().append(openStringQuote()).append(value).append(closeStringQuote()).toString();
	}



	@Override
	public String quoteFullName(final String tableName, final String fieldName) {
		return new StringBuilder().append(tableName)
				.append(".").append(openQuote()).append(fieldName).append(closeQuote()).toString();
	}

	@Override
	public String renameTableSql() {
		return "alter table :oldTableName rename to :newTableName ";
	}

	@Override
	public String renameColumnSql() {
		return "alter table :oldTableName rename column :oldColumn to :newColumn ";
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainCreateSerialNumberFunctionSQL()
	 */
	@Override
	public String obtainCreateSerialNumberFunctionSQL() {
		return "CREATE OR REPLACE FUNCTION\r\n"+
				"       \"createSerialNumber\" \r\n"+
				"          (beanName in \"SysSerialNumber\".\"beanName\"%type,\r\n"+
				"           fieldName in \"SysSerialNumber\".\"fieldName\"%type)\r\n"+
				"RETURN INTEGER\r\n"+
				"IS\r\n"+
				"       currentValue integer := 0;  --定义返回变量\r\n"+
				" \r\n"+
				"PRAGMA AUTONOMOUS_TRANSACTION;\r\n"+

				"BEGIN\r\n"+

				"    --最大数加1\r\n"+
				"    UPDATE \"SysSerialNumber\" SET \"currentValue\" = \"currentValue\"+1 where \"beanName\" = beanName and \"fieldName\" = fieldName\r\n"+
				"    Returning \"currentValue\" Into currentValue; --取出最大数\r\n"+

				"    If(SQL%NOTFOUND) THEN  --第一次向数据库中插入最大数为 1 的记录\r\n"+
				"       INSERT INTO \"SysSerialNumber\"(\"id\",\"beanName\",\"fieldName\",\"currentValue\") values('SSN'|| to_char(sysdate,'yyyyMMddhhmmss')||ABS(MOD(DBMS_RANDOM.RANDOM,10000)),beanName,fieldName,1) ;\r\n"+
				"       currentValue := 1;\r\n"+
				"    End If ;\r\n"+
				"     commit;\r\n"+
				"  return(currentValue); --返回结果\r\n"+
				"end \"createSerialNumber\";";
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainquerySerialNumberFunctionSQL()
	 */
	@Override
	public String obtainQuerySerialNumberFunctionSQL() {
		return "SELECT \"createSerialNumber\"(:beanName,:fieldName) from dual";
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainDecodeSQL(java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public StringBuilder obtainDecodeSQL(Map<String, String> map, String decodeKey, String displayName) {
		StringBuilder sb = new StringBuilder("DECODE(").append(decodeKey);
		for(String code:map.keySet()){
			sb.append(",").append(quoteAsString(code)).append(",").append(quoteAsString(map.get(code)));
		}
		sb.append(") ");
		if(displayName!=null){
			sb.append(quoteKey(displayName));
		}
		return sb;
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainCountSql(java.lang.String)
	 */
	@Override
	public String obtainCountSql(String sqlString){
		StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM ( ")
		.append(sqlString)
		.append(") tmptb");
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#prepareSqlForCount(java.lang.String)
	 */
	@Override
	public String prepareSqlForCount(String sqlString) {
		return sqlString;
	}

	@Override
	public boolean needSetParamForCount() {
		return true;
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#needLowCaseTableName()
	 */
	@Override
	public boolean needLowCaseTableName() {
		return false;
	}


}
