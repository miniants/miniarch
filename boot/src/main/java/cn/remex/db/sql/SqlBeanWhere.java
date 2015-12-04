package cn.remex.db.sql;

import cn.remex.core.exception.IllegalArgumentException;
import cn.remex.db.DbCvo;
import cn.remex.db.rsql.RsqlConstants.WhereGroupOp;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.dialect.Dialect;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2012-4-5
 * 
 */
public class SqlBeanWhere implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8929994826268738990L;

	/**
	 * @param tableName 这是表简名，不是beanName或者表真名。
	 * 
	 * @param sField
	 *            字段名称
	 * @param sOper
	 *            操作名称
	 * @param sParamName
	 *            参数名称，采用i递增，避免冲突
	 * @return String
	 */
	public static String ruleToSQL(final String tableName, final String sField, final String sParamName, final String sOper) {
		Dialect dialect = RDBManager.getLocalSpaceConfig().getDialect();
//		String tableName = beanName;
		String percent = dialect.quoteAsString("%"),
		// 添加beanName避免数据库表的字段名冲突
		fullField = dialect.quoteKey(tableName) + "." + dialect.quoteKey(sField);
		if (sField == null || sField.trim().length() == 0 || sOper == null || sOper.trim().length() == 0) {
			return "";
		}
		StringBuilder cont = new StringBuilder();
		// cont.append(dialect.quoteKey(tableName)).append(".");

		WhereRuleOper ruleOper = WhereRuleOper.valueOf(sOper.trim());
		switch (ruleOper) {
		case eq: // 等于
			cont.append(fullField).append("= :").append(sParamName).append(" ");
			break;
		case ne: // 不等于
			cont.append(fullField).append(" !=  :").append(sParamName).append(" ");
			break;
		case lt: // 小于
			cont.append(fullField).append(" <  :").append(sParamName).append(" ");
			break;
		case le: // 小于等于
			cont.append(fullField).append(" <=  :").append(sParamName).append(" ");
			break;
		case gt: // 大于
			cont.append(fullField).append(" >  :").append(sParamName).append(" ");
			break;
		case ge: // 大于等于
			cont.append(fullField).append(" >=  :").append(sParamName).append(" ");
			break;
		case bw: // 以...开始
			cont.append(fullField).append(" LIKE  ").append(dialect.concat(":"+sParamName,percent) + " ");
			break;
		case bn: // 不以...开始
			cont.append(fullField).append(" NOT LIKE  ").append(dialect.concat(":"+sParamName,percent) + " ");
			break;
		case ew: // 以...结束
			cont.append(fullField).append(" LIKE " + dialect.concat(percent," :" + sParamName) + " ");
			break;
		case en: // 不以...结束
			cont.append(fullField).append(" NOT LIKE " + dialect.concat(percent," :" + sParamName) + " ");
			break;
		case cn: // 包含
			cont.append(fullField).append(" LIKE " + dialect.concat(percent," :" + sParamName,percent) + " ");
			break;
		case nc: // 不包含
			cont.append(fullField).append(" NOT LIKE " + dialect.concat(percent," :" + sParamName,percent) + " ");
		case in: // 在....里面
			cont.append(" :").append(sParamName).append(" LIKE ").append(dialect.concat(percent,fullField,percent)).append(" ");
			break;
		case ni: // 不在....里面
			cont.append(" :").append(sParamName).append(" NOT LIKE ").append(dialect.concat(percent,fullField,percent)).append(" ");
			break;
		case isNull: //值为空
			cont.append(fullField).append(" IS NULL");
			break;
		case notNull: //值不为空
			cont.append(fullField).append(" IS NOT NULL");
			break;
		default:
			// 默认是包含
			cont.append(fullField).append("= :").append(sParamName).append(" ");
			break;
		}
		return cont.toString();
	}

	private boolean search = false; // 是否是查询 true 或者 false

	private List<SqlBeanWhereRule> allRules = new ArrayList<SqlBeanWhereRule>();
	private boolean filter = false;
	private WhereGroupOp groupOp = WhereGroupOp.AND; // 多字段查询时分组类型，主要是AND或者OR

	private List<SqlBeanWhere> groups = new ArrayList<SqlBeanWhere>();// 多组组合
	private String nd; // 暂时不清楚啥用的
	private List<SqlBeanWhereRule> rules = new ArrayList<SqlBeanWhereRule>(); // 多字段查询时候，查询条件的集合
	private boolean searchById = false;

	private String searchField; // 单字段查询的时候，查询字段名称

	private WhereRuleOper searchOper; // 单字段查询的时候，查询的操作

	private String searchString; // 单字段查询的时候，查询字段的值

	public void addGroup(SqlBeanWhere sqlBeanWhere) {
		this.search=true;
		this.groups.add(sqlBeanWhere);
	}

	public void addRule(String field, WhereRuleOper ruleOper, String value) {
		this.search = true;
		String rf = DbCvo.obtainPredicateBeanField(field);
		this.rules.add(new SqlBeanWhereRule(rf, ruleOper, value));
	}

	public List<SqlBeanWhereRule> getAllRules() {
		return this.allRules;
	}

	public WhereGroupOp getGroupOp() {
		return this.groupOp;
	}

	public List<SqlBeanWhere> getGroups() {
		return this.groups;
	}

	public String getNd() {
		return this.nd;
	}

	public List<SqlBeanWhereRule> getRules() {
		return this.rules;
	}

	public String getSearchField() {
		return this.searchField;
	}

	public WhereRuleOper getSearchOper() {
		return this.searchOper;
	}

	public String getSearchString() {
		return this.searchString;
	}

	public boolean isSearch() {
		this.search = isFilter() || this.searchString!=null;
		return this.search;
	}

	public boolean isFilter() {
		this.filter =( this.rules!=null && this.rules.size()>0) || (this.groups !=null && this.groups.size()>0);
		return this.filter;
	}

	public boolean isSearchById() {
		return this.searchById;
	}

	public String obtainKey() {
		StringBuilder builder = new StringBuilder();
		builder.append("search{");
		builder.append(isSearch());
		builder.append(",filter=[");
		builder.append(toSQL("forKey", new ArrayList<SqlBeanNamedParam>(), 0));
		builder.append("],simpleSearch=[");
		builder.append(this.searchField);
		builder.append("_");
		builder.append(this.searchOper);
		builder.append("]}");

		return builder.toString();
	}

	public void setSearch(final boolean search) {
		this.search = search;
	}

//	public void setAllRules(final List<SqlBeanWhereRule> allRules) {
//		this.allRules = allRules;
//	}

	public void setFilter(final boolean filter) {
		this.filter = filter;
	}

	public void setGroupOp(final WhereGroupOp groupOp) {
		this.groupOp = groupOp;
	}

	public void setGroups(final List<SqlBeanWhere> groups) {
		this.groups = groups;
	}

	public void setNd(final String nd) {
		this.nd = nd;
	}

	public void setRules(final List<SqlBeanWhereRule> rules) {
		this.rules = rules;
	}

	public void setSearchById(final boolean searchById) {
		this.searchById = searchById;
	}

	public void setSearchField(final String searchField) {
		this.searchField = searchField;
	}

	public void setSearchOper(final WhereRuleOper searchOper) {
		this.searchOper = searchOper;
	}

	public void setSearchString(final String searchString) {
		this.searchString = searchString;
	}

	public String toSQL(final String beanName, final List<SqlBeanNamedParam> namedParams, final int i) {
		StringBuilder result = new StringBuilder(" WHERE ");
		if (!isSearch()) {
			return " WHERE 1=1 ";
		}
		if (isFilter()) {
			// 多字段的组合查询
			writeSQL(result,this, beanName, namedParams, i, this.allRules);

		} else if (getSearchField() != null && getSearchOper() != null && getSearchString() != null) {
			// 单字段组合
			result.append(ruleToSQL(beanName, getSearchField(), getSearchField(), getSearchOper().toString()));
			namedParams.add(new SqlBeanNamedParam(-1, getSearchField(), Types.CHAR, null));
		}else{
			throw new IllegalArgumentException("设置了本次查询需要进行where搜索，但没有设置规则！");
		}

		return result.toString();
	}

	private static void writeSQL(final StringBuilder result,final SqlBeanWhere sqlBeanWhere, final String beanName, final List<SqlBeanNamedParam> namedParams, int i, final List<SqlBeanWhereRule> allRules) {
		String groupOp = sqlBeanWhere.getGroupOp().toString();
		int rs=0,gs=0;
		boolean hasRules = sqlBeanWhere.rules!=null && ((rs = sqlBeanWhere.rules.size())>0);
		boolean hasGroups = sqlBeanWhere.groups!=null && ((gs = sqlBeanWhere.groups.size())>0);
		
		if(hasRules){
			for (int j = 0,s =rs , gos = s - 1; j < s; j++, i++) {
				SqlBeanWhereRule rule = sqlBeanWhere.rules.get(j);
				result.append(ruleToSQL(beanName, rule.getField(), rule.getField() + i, rule.getOp()));
				namedParams.add(new SqlBeanNamedParam(-1, rule.getField() + i, Types.CHAR, null));
				rule.setParamName(rule.getField() + i);
				allRules.add(rule);
				if (j < gos) {//从倒数第二个开始不用添加AND 或者 OR连接符
					result.append(" ").append(groupOp).append(" ");
				}
			}
		}
		
		if(hasRules && hasGroups)
			result.append(" ").append(groupOp).append(" ");		//rules和groups之间的and 或者or连接符

		// 多组查询
		if (hasGroups) {
			for (int j = 0, s = gs,gos=s-1; j < s; j++) {
				result.append(" (");
				SqlBeanWhere group = sqlBeanWhere.groups.get(j);
				group.setSearch(sqlBeanWhere.search);
				writeSQL(result,group,beanName, namedParams, i, allRules);
				result.append(") ");
				if (j < gos) {//从倒数第二个开始不用添加AND 或者 OR连接符
					result.append(" ").append(groupOp).append(" ");
				}
			}
		}
	}
}
