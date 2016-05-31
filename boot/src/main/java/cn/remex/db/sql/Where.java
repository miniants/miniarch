package cn.remex.db.sql;

import cn.remex.core.exception.IllegalArgumentException;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.Param;
import cn.remex.db.DbCvo;
import cn.remex.db.lambdaapi.WherePredicate;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static cn.remex.db.sql.WhereRuleOper.isNull;
import static cn.remex.db.sql.WhereRuleOper.notNull;

/**
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2012-4-5
 * 
 */
public class Where<T extends Modelable,NT extends Modelable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8929994826268738990L;
	private DbCvo<T> superDbCvo;
    private Where<T,NT> superWhere;

	/**
	 * @param tableName 这是表简名，不是beanName或者表真名。
	 * @param dbCvo  @return String
	 */
	private static <T extends Modelable> String ruleToSQL(String tableName, Param<Integer> tableIndex, WhereRule rule, List<NamedParam> namedParams, Param<Integer> paramIndex, List<WhereRule> allRules,
	                                                      DbCvo<T> dbCvo) {


		String sField = rule.getField(),
				sParamName = rule.getField(),//参数名默认没有加编号，如果(!rule.isSubStatment()) && (ruleOper != isNull) && (ruleOper != notNull)需要加编号
				sOper = rule.getOp();
		WhereRuleOper ruleOper = WhereRuleOper.valueOf(sOper.trim());
		if ((!rule.isSubStatment()) && (ruleOper != isNull) && (ruleOper != notNull)) {//参数名默认没有加编号，如果(!rule.isSubStatment()) && (ruleOper != isNull) && (ruleOper != notNull)需要加编号
			//
			paramIndex.param++;
			sParamName = rule.getField() + paramIndex.param;
			namedParams.add(new NamedParam(paramIndex.param, sParamName, Types.CHAR, null));
			rule.setParamName(sParamName);
			allRules.add(rule);
		}

		Dialect dialect = RDBManager.getLocalSpaceConfig(dbCvo._getSpaceName()).getDialect();
//		String tableName = beanName;
		String percent = dialect.quoteAsString("%"),
		// 添加beanName避免数据库表的字段名冲突
		aliasName = tableName,
		fullField;

		boolean isModelOrListField = sField.indexOf('.')>0;
		if(null!=dbCvo && isModelOrListField) { //只为model list 属性去寻找aliasName，基本字段的表名就是默认的T
			String parentField = sField.substring(0, sField.lastIndexOf('.'));
			Param<String> aliasNameParam = new Param(null);

			//插在该列是否被dbCvo使用并存在
			dbCvo._getRootColumn().anySubColumnMatch(
					p -> !Judgment.nullOrBlank(p.getFieldAliasName()) && parentField.equals(p.getFieldAliasName()),
					c -> aliasNameParam.param = c.getAliasName());
			Assert.notNullAndEmpty(aliasNameParam.param, ServiceCode.RSQL_SQL_ERROR, "where 查询的列不明确:"+parentField);
			aliasName = aliasNameParam.param;
		}

		String useFieldName = isModelOrListField?sField.substring(sField.lastIndexOf('.')+1):sField;
		fullField = dialect.quoteKey(aliasName) + "." + dialect.quoteKey(useFieldName);
		if (sField.trim().length() == 0 || sOper == null || sOper.trim().length() == 0) {
			return "";
		}
		StringBuilder cont = new StringBuilder();
		// cont.append(dialect.quoteKey(tableName)).append(".");


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
			break;
		case in: // 在....里面
			cont.append(" :").append(sParamName).append(" LIKE ").append(dialect.concat(percent,fullField,percent)).append(" ");
			break;
		case inSubSelect: // 在....里面
			rule.getRuleDbCvo()._setParamIndex(paramIndex);
			rule.getRuleDbCvo()._setTableAliasName("SS"+(tableIndex.param++));
			rule.getRuleDbCvo()._setNamedParams(dbCvo._getNamedParams());
			rule.getRuleDbCvo()._initForRsqlDao();
			String ruleSubSelect = rule.getRuleDbCvo()._getPrettySqlString();
            rule.getRuleDbCvo().getParameters().forEach((k, v) -> dbCvo.$S(k, v));
            cont.append(fullField).append(" IN ( ").append(ruleSubSelect).append(" ) ");
			break;
		case notInSubSelect: // 在....里面
			rule.getRuleDbCvo()._setParamIndex(paramIndex);
			rule.getRuleDbCvo()._setTableAliasName("SS"+(tableIndex.param++));
			rule.getRuleDbCvo()._setNamedParams(dbCvo._getNamedParams());
			rule.getRuleDbCvo()._initForRsqlDao();
			String ruleNotInSubSelect = rule.getRuleDbCvo()._getPrettySqlString();
            rule.getRuleDbCvo().getParameters().forEach((k, v) -> dbCvo.$S(k, v));
            cont.append(fullField).append(" NOT IN ( ").append(ruleNotInSubSelect).append(" ) ");
			break;
		case existSubSelect: // 存在子句结果为true
			rule.getRuleDbCvo()._setParamIndex(paramIndex);
			rule.getRuleDbCvo()._setTableAliasName("SS");
			rule.getRuleDbCvo()._setNamedParams(dbCvo._getNamedParams());
			rule.getRuleDbCvo()._initForRsqlDao();
			String ruleExistSubSelect = rule.getRuleDbCvo()._getPrettySqlString();
			rule.getRuleDbCvo().getParameters().forEach((k, v) -> dbCvo.$S(k, v));
			cont.append(fullField).append(" EXIST ( ").append(ruleExistSubSelect).append(" ) ");
			break;
		case notExistSubSelect: // 存在子句结果为true
			rule.getRuleDbCvo()._setParamIndex(paramIndex);
			rule.getRuleDbCvo()._setTableAliasName("SS");
			rule.getRuleDbCvo()._setNamedParams(dbCvo._getNamedParams());
			rule.getRuleDbCvo()._initForRsqlDao();
			String ruleNotExistSubSelect = rule.getRuleDbCvo()._getPrettySqlString();
			rule.getRuleDbCvo().getParameters().forEach((k, v) -> dbCvo.$S(k, v));
			cont.append(fullField).append(" NOT EXIST ( ").append(ruleNotExistSubSelect).append(" ) ");
			break;
		case ni: // 不在....里面
			cont.append(" :").append(sParamName).append(" NOT LIKE ").append(dialect.concat(percent,fullField,percent)).append(" ");
			break;
		case isNull: //值为空
			cont.append(fullField).append(" IS NULL ");
			break;
		case notNull: //值不为空
			cont.append(fullField).append(" IS NOT NULL ");
			break;
		default:
			// 默认是包含
			cont.append(fullField).append("= :").append(sParamName).append(" ");
			break;
		}
		return cont.toString();
	}
	private static void writeSQL(final StringBuilder result, final Where where, final String beanName,
	                             Param<Integer> tableIndex, final List<NamedParam> namedParams, Param<Integer> paramIndex, final List<WhereRule> allRules, final DbCvo dbCvo) {

		String groupOp = where.getGroupOp().toString();
		int rs=0,gs=0;
		boolean hasRules = where.rules!=null && ((rs = where.rules.size())>0);
		boolean hasGroups = where.groups!=null && ((gs = where.groups.size())>0);

		if(hasRules){
			for (int j = 0,s =rs , gos = s - 1; j < s; j++) {
				WhereRule rule = (WhereRule) where.rules.get(j);
				result.append(ruleToSQL(beanName, tableIndex, rule, namedParams, paramIndex, allRules, dbCvo));
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
				Where group = (Where) where.groups.get(j);
				group.setSearch(where.search);
				writeSQL(result,group,beanName, tableIndex, namedParams, paramIndex, allRules,dbCvo);
				result.append(") ");
				if (j < gos) {//从倒数第二个开始不用添加AND 或者 OR连接符
					result.append(" ").append(groupOp).append(" ");
				}
			}
		}
	}
	private boolean search = false; // 是否是查询 true 或者 false

	private List<WhereRule> allRules = new ArrayList<>();
	private boolean filter = false;
	private WhereGroupOp groupOp = WhereGroupOp.AND; // 多字段查询时分组类型，主要是AND或者OR

	private List<Where<T,NT>> groups = new ArrayList<>();// 多组组合
	private String nd; // 暂时不清楚啥用的
	private List<WhereRule> rules = new ArrayList<>(); // 多字段查询时候，查询条件的集合

	private String searchField; // 单字段查询的时候，查询字段名称

	private WhereRuleOper searchOper; // 单字段查询的时候，查询的操作

	private String searchString; // 单字段查询的时候，查询字段的值


	public Where<T,NT> filterByGroup(Consumer<Where<T,NT>> groupConsumer){

		Where<T,NT> group = new Where<>();
        group.setSuperDbCvo(this.superDbCvo);
        group.setSuperWhere(this);
		this.addGroup(group);

		groupConsumer.accept(group);

		return this;
	}

    /**
     * 完成整个where 的构建,返回DbCvo
     * @return
     */
	public DbCvo<T> end(){
		return this.superDbCvo;
	}

    /**
     *  完成当前where的构建,返回上一级where
     *
     *  @return
     */
    public Where<T,NT> endGroup(){
        return this.superWhere;
    }

    /**
     * 一个可以AND 或者OR的规则
     *
     * @param wp
     * @param oper
     * @param value
     * @return
     */
    public Where<T,NT> filterBy(WherePredicate<NT> wp, WhereRuleOper oper, String value) {
//        wp.init(superDbCvo._obtainAOPBean());
//        String fieldName = superDbCvo.obtainPredicateBeanField(null);
//        this.addRule(fieldName, oper, value);
		ReflectUtil.eachFieldWhenGet(superDbCvo._obtainAOPBean(), b -> wp.init((NT) b), s -> this.addRule((String) s, oper, value));
		return this;
    }

    /**
     * 规则AND  OR
     * @param whereGroupOp
     * @return
     */
    public Where<T,NT> filterOper(WhereGroupOp whereGroupOp) {
        this.setGroupOp(whereGroupOp);
        return this;
    }


	public void addGroup(Where where) {
		this.search=true;
		this.groups.add(where);
	}

	public void addRule(String field, WhereRuleOper ruleOper, Object value) {
		this.search = true;
		String rf = DbCvo.obtainPredicateBeanField(field);
		this.rules.add(new WhereRule(rf, ruleOper, value));
	}
	public void addSubSelectRule(String field, WhereRuleOper ruleOper, DbCvo ruleDbCvo) {
		this.search = true;
		this.rules.add(new WhereRule(field, ruleOper, ruleDbCvo));
	}

	public List<WhereRule> getAllRules() {
		return this.allRules;
	}

	public WhereGroupOp getGroupOp() {
		return this.groupOp;
	}

	public List<Where<T,NT>> getGroups() {
		return this.groups;
	}

	public String getNd() {
		return this.nd;
	}

	public List<WhereRule> getRules() {
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

	public void setSearch(final boolean search) {
		this.search = search;
	}

//	public void setAllRules(final List<WhereRule> allRules) {
//		this.allRules = allRules;
//	}

	public void setFilter(final boolean filter) {
		this.filter = filter;
	}

	public void setGroupOp(final WhereGroupOp groupOp) {
		this.groupOp = groupOp;
	}

	public void setGroups(final List<Where<T,NT>> groups) {
		this.groups = groups;
	}

	public void setNd(final String nd) {
		this.nd = nd;
	}

	public void setRules(final List<WhereRule> rules) {
		this.rules = rules;
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

	/**
	 * @param needWhere 是否需要where 关键字开头
	 * @param tableIndex 命名参数的序号
	 * @return
	 */
	public String toSQL(boolean needWhere, final String beanName, final List<NamedParam> namedParams, final Param<Integer> paramIndex, final Param<Integer> tableIndex, final DbCvo dbCvo) {
		StringBuilder result = new StringBuilder(needWhere?" WHERE ":" ");
		if (!isSearch()) {
			return "";
		}
		if (isFilter()) {
			// 多字段的组合查询
			writeSQL(result,this, beanName, tableIndex, namedParams, paramIndex, this.allRules, dbCvo);

		} else if (getSearchField() != null && getSearchOper() != null && getSearchString() != null) {
			// 单字段组合
			throw new IllegalArgumentException("现在项目已经删除了对简单字段搜索的支持了！");
		}else{
			throw new IllegalArgumentException("设置了本次查询需要进行where搜索，但没有设置规则！");
		}

		return result.toString();
	}

	public void setSuperDbCvo(DbCvo<T> superDbCvo) {
		this.superDbCvo = superDbCvo;
	}


    public Where<T,NT> getSuperWhere() {
        return superWhere;
    }

    public void setSuperWhere(Where<T,NT> superWhere) {
        this.superWhere = superWhere;
    }

    public DbCvo<T> getSuperDbCvo() {
        return superDbCvo;
    }


	public Where<T,NT> everyRule(Consumer<WhereRule> consumer){
		this.rules.forEach(consumer::accept);
		this.groups.forEach(g->g.everyRule(consumer::accept));
		return this;
	}
}
