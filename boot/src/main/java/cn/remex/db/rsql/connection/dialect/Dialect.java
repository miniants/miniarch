/*
 * 文 件 名 : Snippet.java
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

import java.sql.Types;
import java.util.Map;


/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-2-25
 *
 */

public abstract class Dialect{
	
	public abstract boolean needLowCaseTableName();
	public abstract String obtainCreateSerialNumberFunctionSQL();
	public abstract String obtainQuerySerialNumberFunctionSQL();
	public abstract StringBuilder obtainDecodeSQL(Map<String, String> map,final String decodeKey,final String displayName );
	/**
	 * 返回限定的属性名称。tableAliasName将不会进行quote。
	 * 
	 * @param tableAliasName
	 * @param fieldName
	 * @param aliasName
	 * @return String
	 */
	public abstract String aliasFullName(String tableAliasName, String fieldName, String aliasName);
	/**
	 * 将数据中限定的表名 去一个 aliasName。
	 * 
	 * @param tableName 区分大小写，需要进行quote
	 * @param aliasName 不区分大小写，不进行quote
	 * @return String
	 */
	public abstract String aliasTableName(String tableName, String aliasName);
	public abstract char closeQuote();
	public abstract char closeStringQuote();
	
	/**
	 * concat(false,"a","bbb") return "abb";<br>
	 * concat(true,"a","'bbb'") return "a'b'"<br>
	 * 需要quote的字符串需要传参前进行处理。
	 * @param strs 需要一次链接起来的字符串
	 * @return
	 * 
	 * 
	 */
	public abstract String concat(String... strs);

	/**
	 * 约束类型默认为 唯一性约束
	 * 
	 * @param beanName
	 * @param name 
	 * @param columnNames
	 * @return String
	 */
	public String obtainConstraintSql(String beanName, String name, String... columnNames) {

		StringBuilder sb = new StringBuilder("ALTER TABLE ")
		.append(quoteKey(beanName))
		.append(" ADD CONSTRAINT ")
		.append(name)
		.append(" UNIQUE ( ");
		for (String c : columnNames) {
			sb.append(quoteKey(c)).append(",");
		}
		sb.deleteCharAt(sb.length()-1).append(")");
		return sb.toString();

	}
	public abstract String obtainCountSql(String sqlString);
	public abstract boolean needSetParamForCount();
	public abstract String prepareSqlForCount(String sqlString);
	/**
	 * @rmx.summary indexName的生成方法为：
	 * beanName中的大写字母+"_"+columnNames的各首字符连接+各个列连接字符串的hash值
	 * 最后全部大写
	 * 
	 * @param beanName
	 * @param columnNames
	 * @return String
	 */
	public String obtainIndexName(String beanName,String... columnNames){
		StringBuilder idxname=new StringBuilder(beanName.replaceAll("[^A-Z]", "")).append("_");
		StringBuilder cols=new StringBuilder(beanName);
		for(String c:columnNames){
			idxname.append(c.charAt(0));
			cols.append(c);
		}
		return idxname.append(String.valueOf(cols.toString().hashCode()).replaceAll("\\-","_")).toString().toUpperCase();
	}
	public String obtainIndexSql(String beanName, String... columnNames) {

		StringBuilder sb = new StringBuilder("CREATE INDEX ")
		.append(obtainIndexName(beanName, columnNames))
		.append(" ON ")
		.append(quoteKey(beanName))
		.append(" ( ");
		for (String c : columnNames) {
			sb.append(quoteKey(c)).append(",");
		}
		sb.deleteCharAt(sb.length()-1).append(")");
		return sb.toString();

	}
	public abstract String obtainPagingSQL(String sqlString,long start,long end, long rowCount);

	public abstract String obtainSelectRegex() ;
	
	public abstract String obtainSQLSelectIndexs(String beanName) ;
	/**
	 * 获取 当前方言中标示index的名字
	 * @param beanName
	 * @return
	 */
	public abstract String obtainSQLIndexNameField() ;
	
	/**
	 * @return String
	 */
	public abstract String obtainSQLSelectTableNames() ;
	/**
	 * @param beanName
	 * @return String
	 */
	public abstract String obtainSQLSelectTablesColumnNames(String beanName) ;
	/**
	 * @param integer  {@link Types} 
	 * @return String
	 */
	public abstract String obtainSQLTypeString(int integer);
	/**
	 * 
	 * @param integer  {@link Types} 
	 * @param length
	 * @return String
	 */
	public abstract String obtainSQLTypeString(int integer,int length);
	public abstract char openQuote();
	public abstract char openStringQuote();
	public abstract String quoteAsString(Object value);
	/**
	 * 
	 * 采用方言指定的符号对字段进行quot，其中表名是不做处理的，
	 * @param tableName 表名，是不需要进行处理的表名可直接使用。如oracle下，即：tableName."fieldName"
	 * @param fieldName
	 * @return String
	 */
	public abstract String quoteFullName(String tableName,String fieldName);
	/**
	 * 将指定的keys进行连续quote。如oracle中，【"roles"."staff"."name"】，采用 quoteKey("roles","staff","name")
	 * @param keys
	 * @return String
	 */
	public String quoteKey(String... keys){
		StringBuilder sb = new StringBuilder();
		for(String key:keys) {
			sb.append(openQuote()).append(key).append(closeQuote()).append(".");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}

