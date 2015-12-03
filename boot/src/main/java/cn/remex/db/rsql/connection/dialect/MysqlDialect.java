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

import cn.remex.db.exception.RsqlTypeException;

import java.sql.Types;
import java.util.Map;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-2-25
 *
 */
public class MysqlDialect extends Dialect {
	@Override
	public String aliasFullName(final String tableAliasName, final String fieldName,final String aliasName){
		return new StringBuilder().append(tableAliasName)
				.append(".").append(openQuote()).append(fieldName).append(closeQuote())
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
		return '`';
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
		StringBuffer sb = new StringBuffer("CONCAT(");
		int c=strings.length-1;
		for(int i=0;i<c;i++){
			sb.append(strings[i]).append(",");
		}
		return sb.append(strings[c]).append(")").toString();
	}
	
	@Override
	public String obtainPagingSQL(String sqlString,long start,long end,long rowCount) {
//		StringBuilder sb = new StringBuilder("SELECT * FROM (SELECT A.*,ROWNUM rn FROM ( ")
//		.append(sqlString)
//		.append(") A WHERE ROWNUM<=").append(end).append(") WHERE rn>").append(start);
		StringBuilder sb = new StringBuilder(sqlString)
		.append(" LIMIT ").append(start).append(",").append(rowCount);//TODO 大数据分页存在性能问题。
		
		return sb.toString();
	}

	@Override
	public String obtainSQLSelectIndexs(String beanName) {
		return "SHOW Index from " + quoteKey(beanName);
	}
	
	@Override
	public String obtainSQLIndexNameField() {
		return "Key_name";
	}

	@Override
	public String obtainSQLSelectTableNames() {
		return "select table_name from information_schema.tables where table_schema in (select database() )";
	}

	@Override
	public String obtainSQLSelectTablesColumnNames(final String tableName) {
		StringBuilder sqlString = new StringBuilder();
		sqlString.append("select t.COLUMN_NAME,t.DATA_TYPE DATA_TYPE,t.CHARACTER_MAXIMUM_LENGTH DATA_LENGTH,t.COLUMN_COMMENT COMMENTS from information_schema.columns t  where table_schema in (select database() ) and table_name= "+quoteAsString(tableName));
//
//		sqlString.append("select t.COLUMN_NAME,DATA_TYPE,DATA_LENGTH,c.COMMENTS " +
//				"from user_tab_columns t,user_col_comments c " +
//				"where t.table_name = c.table_name and t.column_name = c.column_name " +
//				"and t.table_name = :tableName");
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
			return " INTEGER ";
		case Types.DOUBLE:
			return " DOUBLE ";
		case Types.FLOAT:
			return " FLOAT ";
		case Types.BOOLEAN:
			return " VARCHAR(5) ";
		case Types.BIT:
			return " BIT ";
		case Types.CHAR:
			return " VARCHAR("+(length==-1?600:length)+") ";
		case Types.DATE:
			return " VARCHAR(20) ";
		case Types.JAVA_OBJECT:
			return " VARCHAR(2000) ";
		case Types.CLOB:
			return " TEXT ";

		default:
			throw new RsqlTypeException(type,
					"未定义的SQLTypes!");
			//			return null;// never arrive here
		}
	}

	@Override
	public char openQuote() {
		// TODO Auto-generated method stub
		return '`';
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

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainCreateSerialNumberFunctionSQL()
	 */
	@Override
	public String obtainCreateSerialNumberFunctionSQL() {
		String sql = "DELIMITER $$ \n "+
				"DROP FUNCTION IF EXISTS `createSerialNumber` $$\n "+
				"CREATE FUNCTION `createSerialNumber`(beanName varchar(255), fieldName varchar(255)) RETURNS INTEGER\n "+
				"BEGIN\n "+
				"	declare cv int;\n "+
				"	declare cnt int default 0;\n "+
				"	/*最大数加1*/\n "+
				"	UPDATE `SysSerialNumber` SET  `currentValue` =(SELECT @cv:= (`currentValue`+1)) where `beanName` = beanName and `fieldName` = fieldName LIMIT 1;\n "+
				"    set cv =( select @cv);\n "+
				"    set cnt = (select row_count());\n "+
				"    if cnt = 0 then\n "+
				"		INSERT INTO `SysSerialNumber` (`id`,`beanName`,`fieldName`,`currentValue`) values(concat('SSN',date_format(now(),'%Y%m%d%H%i%S'),FLOOR(1000+ (RAND() * 1000))),beanName,fieldName,1) ;\n "+
				"    end if;\n "+
				"RETURN cv;\n "+
				"END $$\n "+
				"DELIMITER ;\n ";
		
		
		return sql;
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainquerySerialNumberFunctionSQL()
	 */
	@Override
	public String obtainQuerySerialNumberFunctionSQL() {
		return "SELECT `createSerialNumber`(:beanName,:fieldName) from dual";
	}
	@Override
	public StringBuilder obtainDecodeSQL(Map<String, String> map,final String decodeKey,final String displayName ){
		StringBuilder sb = new StringBuilder("case ").append(decodeKey);
		for(String code:map.keySet()){
			sb.append(" when ").append(quoteAsString(code)).append(" then ").append(quoteAsString(map.get(code)));
		}
		sb.append(" end ");
		if(displayName!=null){
			sb.append(quoteKey(displayName));
		}

		return sb;
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#obtainCountSql(java.lang.String)
	 */
	@Override
	public String obtainCountSql(String sqlString) {
		return "SELECT FOUND_ROWS() AS count;";
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#prepareSqlForCount(java.lang.String)
	 */
	@Override
	public String prepareSqlForCount(String sqlString) {
		return sqlString.replaceFirst("(SELECT)|(Select)|(select)\\s", "SELECT SQL_CALC_FOUND_ROWS ");
	}

	@Override
	public boolean needSetParamForCount() {
		return false;
	}

	/* (non-Javadoc)
	 * @see cn.remex.db.rsql.connection.dialect.Dialect#needLowCaseTableName()
	 */
	@Override
	public boolean needLowCaseTableName() {
		return true;
	}


}
