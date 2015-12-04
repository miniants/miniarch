package cn.remex.db.rsql;

import cn.remex.core.util.Assert;
import cn.remex.core.util.StringHelper;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.WhereRuleOper;
import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MatchResult;

import java.util.Map;

public class ReplaceRefCode {
	static Logger logger = RsqlConstants.logger;

	/**
	 * @param orgnSql
	 * @return
	 */
	public static String replaceRefCode(String orgnSql){
		// 条件匹配  表.列,别名,匹配表,匹配表条件列,条件,（条件咧=条件）,被匹配列,被匹配名
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		final char t = dialect.openQuote() ;
		final  String RefCodeRegx = "RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		final  String RefCodeRegx5 = "RefCode\\(substr\\((\\w+)\\."+t+"(\\w+)"+t+",0,2\\),"+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		final  String RefCodeRegx6 = "RefCode\\(substr\\((\\w+)\\."+t+"(\\w+)"+t+",0,2\\),"+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 无条件匹配     表.列,别名,匹配表,被匹配列,被匹配名
		final String RefCodeRegx2="RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 条件无别名匹配     表.列,匹配表,匹配表条件列,条件,（条件咧=条件）,被匹配列,被匹配名
		final  String RefCodeRegx3 = "RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 无条件无别名匹配    表.列 , 匹配表,被匹配列,被匹配名
		final String RefCodeRegx4="RefCode\\((\\w+)\\."+t+"(\\w+)"+t+","+t+"(\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+","+t+"(\\w+|\\w+\\.\\w+)"+t+"\\)";
		// 省 查到市
		MatchResult rs;
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String displayName = rs.group(3);
			String codeTbName = rs.group(4);
			String refTypeColumn = rs.group(5);
			String refCodeType = rs.group(6);
			String codeColumn = rs.group(7);
			String descColumn = rs.group(8);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx5, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String displayName = rs.group(3);
			String codeTbName = rs.group(4);
			String refTypeColumn = rs.group(5);
			String refCodeType = rs.group(6);
			String codeColumn = rs.group(7);
			String descColumn = rs.group(8);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,true);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx5, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx2, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String displayName = rs.group(3);
			String codeTbName = rs.group(4);
			String codeColumn = rs.group(5);
			String descColumn = rs.group(6);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,displayName,codeTbName,null,null,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx2,decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx3, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String codeTbName = rs.group(3);
			String refTypeColumn = rs.group(4);
			String refCodeType = rs.group(5);
			String codeColumn = rs.group(6);
			String descColumn = rs.group(7);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,null,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx3, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx6, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String codeTbName = rs.group(3);
			String refTypeColumn = rs.group(4);
			String refCodeType = rs.group(5);
			String codeColumn = rs.group(6);
			String descColumn = rs.group(7);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,null,codeTbName,refTypeColumn,refCodeType,codeColumn,descColumn,true);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx6, decodeSqlPart.toString(),1);
		}
		while(null!=(rs = StringHelper.match(orgnSql, RefCodeRegx4, null))){
			String fieldTable = rs.group(1);
			String fieldName = rs.group(2);
			String codeTbName = rs.group(3);
			String codeColumn = rs.group(4);
			String descColumn = rs.group(5);

			StringBuilder decodeSqlPart = obtainDecodeSqlPart(fieldTable,fieldName,null,codeTbName,null,null,codeColumn,descColumn,false);

			orgnSql = StringHelper.substitute(orgnSql, RefCodeRegx4, decodeSqlPart.toString(),1);
		}
		return orgnSql;
	}
	public static void main(String[] args) {
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		final char t = dialect.openQuote() ;
		String sql= "RefCode(substr(atin."+t+"cityCode"+t+",0,2),"+t+"aaa"+t+","+t+"CityCode"+t+","+t+"code"+t+","+t+"110100"+t+","+t+"name"+t+","+t+"code"+t+")";
		replaceRefCode(sql);
	}

	private static StringBuilder obtainDecodeSqlPart(
			final String fieldTable,  //表明
			final String fieldName,		// 表列
			final String displayName,	// 表别名
			final String codeTableName,	// 关联表名
			final String refTypeColumn, // 关联列 的筛选    用于筛选
			final String refCodeType,	// 筛选的值
			final String codeColumn,	// 关联列
			final String descColumn,		// 关联列的值
			final boolean hasSubStr //需要decode的sql中存在substr函数
			){
		RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig();
		Dialect dialect = spaceConfig.getDialect();
		@SuppressWarnings("unchecked")
		Class<Modelable> clazz = (Class<Modelable>) spaceConfig.getOrmBeanClass(codeTableName);
		DbRvo rvo = ContainerFactory.getSession().query(new DbCvo<Modelable>(clazz){
			private static final long serialVersionUID = -1322957928866739488L;
			@Override
			public void initRules(Modelable t) {
				if(null!=refTypeColumn && null!=refCodeType)
					addRule(refTypeColumn, WhereRuleOper.eq, refCodeType);
			}
		});
		Map<String, String> map = rvo.obtainMap(codeColumn, descColumn);

		Assert.isTrue(map!=null && map.size()>0, "配置的值参照中没有相应的参数！请配置人员查阅数据表:"+codeTableName);
		return  dialect.obtainDecodeSQL(map, hasSubStr?"substr("+dialect.quoteFullName(fieldTable, fieldName)+",0,2)":dialect.quoteFullName(fieldTable, fieldName), displayName);
	}

}
