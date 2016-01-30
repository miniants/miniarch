package cn.remex.db;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.Param;
import cn.remex.core.util.StringHelper;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.exception.RsqlInitException;
import cn.remex.db.lambdaapi.ColumnPredicate;
import cn.remex.db.lambdaapi.ListColumnPredicate;
import cn.remex.db.lambdaapi.ModelColumnPredicate;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.sqlutil.Node;
import cn.remex.db.sql.*;
import cn.remex.db.lambdaapi.WherePredicate;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Consumer;

public class DbCvo<T extends Modelable> extends DbCvoBase<T> {

    //====内部属性======================//
    //基于lambda开发的数据操作功能而新增的属性，后面将以上功能逐步迁移过来结构化
    /**
     * 用于通过切面捕获lambda表达式中调用get方法而获得field的.
     */
    protected T aopBean;
    protected SqlColumn<T, T, T> rootColumn;
    protected boolean _hasGroupBy = false;
    public boolean _isHasGroupBy(){
        return _hasGroupBy;
    }
    public void _setHasGroupBy(boolean b){
        _hasGroupBy = b;
    }
    public T obtainAOPBean() {
        return aopBean != null ? aopBean : (aopBean = ReflectUtil.createAopBean(beanClass));
    }


    //新版DbCvo Api接口提供Sql操作能力
    //====filter function======================//
    //filter 开头的函数都是基础当前表的筛选函数，生成where自己的内容。where字句还收到with方法内联对象的filter*方法影响
    public DbCvo<T> filterBy(WherePredicate<T> wp, WhereRuleOper oper, String value) {
        ReflectUtil.eachFieldWhenGet(obtainAOPBean(), b -> wp.init((T) b), s -> this.addRule((String) s, oper, value));
        return this;
    }
    public DbCvo<T> filterOper(WhereGroupOp groupOp) {
        this.filter.setGroupOp(groupOp);
        return this;
    }
    public DbCvo<T> filterByGroup(Consumer<SqlBeanWhere<T, T>> groupConsumer) {
        this.filter.setSuperDbCvo(this);
        SqlBeanWhere<T, T> group = new SqlBeanWhere<>();
        group.setSuperDbCvo(this);
        group.setSuperSqlBeanWhere(this.filter);
        this.filter.addGroup(group);

        groupConsumer.accept(group);

        return this;
//        this.filter.setSuperDbCvo(this);
//        SqlBeanWhere<T, T> group = new SqlBeanWhere<>();
//        group.setSuperDbCvo(this);
//        group.setSuperSqlBeanWhere(this.filter);
//        this.filter.addGroup(group);
//        return group;
    }
    public DbCvo<T> filterById(String id) {
        this.filterBy(Modelable::getId, WhereRuleOper.eq, id);
        return this;
    }
    public DbCvo<T> filter(SqlBeanWhere filter){
        this.setFilter(filter);
        return this;
    }
    /**
     * guoqianyou
     * 此方法 通过传入model 中的属性值 作为 条件 获取 List
     * 没有解决list，与多级问题
     *
     * LHY 2016-1-17 rename listBy -> filterByBean
     *
     * @param model 必须和当前model 相同
     * @return 返回list
     */
    public DbCvo<T> filterByBean(T model) {
        Assert.isTrue(model.getClass().isAssignableFrom(beanClass), "必须传入查询表对应的Model类型的实例对象。");

        Map<String, Method> baseGetters = SqlType.getGetters(beanClass, FieldType.TBase);
        Map<String, Method> objGetters = SqlType.getGetters(beanClass, FieldType.TObject);

        for (String fieldName : baseGetters.keySet()) {
            Object value = ReflectUtil.invokeGetter(fieldName, model);
            if (Judgment.nullOrBlank(value) || value.equals(0))
                continue;
            this.addRule(fieldName, WhereRuleOper.eq, String.valueOf(value));
        }

        for (String fieldName : objGetters.keySet()) {
            Object value = ReflectUtil.invokeGetter(fieldName, model);
            if (Judgment.nullOrBlank(value) || value.equals(0))
                continue;
            this.addRule(fieldName, WhereRuleOper.eq, String.valueOf(ReflectUtil.invokeGetter("id", value)));
        }

        return this;
    }

    //====order======================//
    //order 开头的函数都是排序函数
    public DbCvo<T> orderBy(String sidx, String sord) {
        this.addOrder(true, sidx, sord);
        return this;
    }
    public DbCvo<T> orderBy(WherePredicate<T> wp, Sort s) {
        ReflectUtil.eachFieldWhenGet(obtainAOPBean(), b -> wp.init((T) b), s1 -> this.addOrder(true, s1, s.toString()));
        return this;
    }

    //====group======================//

    //====page control======================//
    //分页函数
    public DbCvo<T> page(int page){
        setPagination(page);
        return this;
    }
    public DbCvo<T> rowCount(int page){
        setRowCount(page);
        return this;
    }

    //====With ======================//
    //with 开头的方法均控制语句的输出字段或更新字段
    public DbCvo<T> withColumns(ColumnPredicate<T> cp, Consumer<SqlColumn<T, T, ?>> sqlColumnConsumer) {
        rootColumn.withColumns(cp, sqlColumnConsumer);
        return this;
    }
    public DbCvo<T> withColumns(ColumnPredicate<T> cp) {
        rootColumn.withColumns(cp);
        return this;
    }
    public DbCvo<T> withColumns(String fieldName, Consumer<SqlColumn<T, T, ?>> sqlColumnConsumer) {
        rootColumn.withColumns(fieldName,sqlColumnConsumer);
        return this;
    }
    public DbCvo<T> withColumns(String fieldName) {
        rootColumn.withColumns(fieldName);
        return this;
    }
    public DbCvo<T> withColumns(){
        rootColumn.withColumns();
        return this;
    }
    public <ST extends Modelable> DbCvo<T> withModel(ModelColumnPredicate<T, T, ST> mcp, Consumer<SqlColumn<T, T, ST>> sqlColumnConsumer) {
        rootColumn.withModelColumn(mcp, sqlColumnConsumer);
        return this;
    }
    public <ST extends Modelable> DbCvo<T> withModel(ModelColumnPredicate<T, T, ST> mcp) {
        rootColumn.withModelColumn(mcp);
        return this;
    }
    public <ST extends Modelable> DbCvo<T> withModel(String fieldName, Consumer<SqlColumn<T, T, ST>> sqlColumnConsumer) {
        rootColumn.withModelColumn(fieldName,sqlColumnConsumer);
        return this;
    }
    public <ST extends Modelable> DbCvo<T> withModel(String fieldName) {
        rootColumn.withModelColumn(fieldName);
        return this;
    }

    public <ST extends Modelable> DbCvo<T> withList(ListColumnPredicate<T, T, ST> mcp, Consumer<SqlColumn<T, T, ST>> sqlColumnConsumer) {
        rootColumn.withList(mcp, sqlColumnConsumer);
        return this;
    }
    public <ST extends Modelable> DbCvo<T> withList(ListColumnPredicate<T, T, ST> mcp) {
        rootColumn.withList(mcp);
        return this;
    }

    //====function for FE======================//
    //为前端准备的方法
    public static final String regx = "([A-Za-z_][A-Za-z\\d_,]*)$|(([A-Za-z_][A-Za-z\\d_]*)\\.)|(([A-Za-z_][A-Za-z\\d_]*)\\[\\]\\.)";
    /**
     * 通过字符串添加属性列.字符串表达式支持链式访问包括 . [] :
     * 通过分号可以一次添加多个
     *
     * @param columnExprs
     * @return
     */
    public DbCvo<T> withExprColumns(String columnExprs) {
        boolean[] end = {false};
        Param param = new Param(null);
        for (String expr : columnExprs.split(";")) {
            end[0] = false;
            param.param = null;
            StringHelper.forEachMatch(expr, regx, (m, b) -> {
                String baseField = m.group(1), objField = m.group(3), listField = m.group(5);
                if (baseField != null) {
                    for (String field : baseField.split(",")) {
                        if (param.param == null) {
                            this.withColumns(field);
                        } else
                            ((SqlColumn) param.param).withColumns(field);
                    }
                } else if (objField != null) {
                    if (param.param == null) {
                        param.param = this.withModel(objField);
                    } else
                        param.param = ((SqlColumn) param.param).withModelColumn(baseField);
                }
                end[0] = b;
            });
            Assert.isTrue(end[0], "表达式不合法，只允许name.   name[].  name 三种格式!");
        }

        return this;
    }

    //将要废弃的方法
    /**
     * 此方法被withColumn方法替代.如果是前端传过来的参数结构调整为
     * {}
     *
     * @return
     */
    @Deprecated
    public DbCvo<T> putExtColumn(final String extColumns) {
        for (String extColumn : extColumns.split(";")) {
            String[] columns = extColumn.split("\\.");
            Assert.isTrue(columns.length == 2, "putExtColumn方法只能支持一层Object属性拓展");

//            SqlColumn<T, T, ST> sc = new SqlColumn<>(this, null);
//            Class<ST> nodeClass = (Class<ST>) SqlType.getFields(beanClass, SqlType.FieldType.TObject).get(columns[0].toString());
//            ST nodeBean = ReflectUtil.createAopBean(nodeClass);
//            sc.init(columns[0], nodeBean, nodeClass, nodeClass, SqlType.FieldType.TObject);
//            this.addColumn(sc);//添加到DbCvo中将来dataCloumns属性将不再使用

            rootColumn.withModelColumn(columns[0], m -> m.withColumns(columns[1]));
        }
        return this;
    }


    //====core method======================//
    //原来写好的方法，很核心，有优化，基本不用大动
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
    public DbCvo(Class<T> beanClass, SqlOper oper) {
        Assert.notNull(oper, "数据库操作符oper不能为空！");
        this.oper = oper;
        init(beanClass);
    }
    public DbCvo(Class<T> beanClass, SqlOper oper, String dataType) {
        Assert.notNull(oper, "数据库操作符oper不能为空！");
        this.oper = oper;
        if (null != dataType) this.dataType = dataType;
        init(beanClass);
    }
    public DbCvo(Class<T> beanClass, SqlOper oper, String dataType, String dataColumns) {
        Assert.notNull(oper, "数据库操作符oper不能为空！");
        this.oper = oper;
        if (null != dataType) this.dataType = dataType;
        if (null != dataColumns) this.dataColumns = dataColumns;
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
    /**
     * init方法仅且必须被所有DbCvo的构造函数调用.
     * @param clazz
     */
    @SuppressWarnings("unchecked")
    public void init(final Class<T> clazz) {
        Class<T> clazz1=clazz;
        if(null != clazz1){
            try {
                clazz1 = (Class<T>) Class.forName(StringHelper.getClassName(clazz1));
            } catch (ClassNotFoundException e) {
                throw new RsqlInitException("初始化DbCvo时,beanClass异常！",e);
            }
            T t = InnerDbCvoArgBeanFactory.getBean(clazz1);
            initRules(t);
            this.beanName = StringHelper.getClassSimpleName(clazz1);
        }else{
            this.beanName = "sqlString_" + sqlString.hashCode();
        }

        this.beanClass = clazz1;

        this.spaceName = null == this.spaceName ? RDBManager.getLocalSpaceConfig().getSpace() : this.spaceName;

        if(null!=beanClass)this.rootColumn  = new SqlColumn(this,beanClass, obtainAOPBean());
    }
    public void initParam() {
        this.isInit = true;

        //LHY 2015-2-17 此处可添加在数据库操作之前必须检查的逻辑
        if (null != this.dataColumns && null != beanClass) {
            Map<String, Type> allfields = SqlType.getFields(beanClass, FieldType.TAll);
            for (String field : this.dataColumns.split(";")) {
                if (!allfields.containsKey(field))
                    throw new RsqlExecuteException("执行数据操作时发现，数据库模型" + beanName + "中不存在属性" + field);
            }
        }

        this.sqlBean = SqlBean.getInstance(this);
        this.sqlBean.initParam(this);
    }

    //====getter and setter======================//
    public SqlColumn<T, T, T> getRootColumn() {
        return rootColumn;
    }
}
