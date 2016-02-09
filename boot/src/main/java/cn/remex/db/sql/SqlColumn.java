package cn.remex.db.sql;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.db.DbCvo;
import cn.remex.db.lambdaapi.ColumnPredicate;
import cn.remex.db.lambdaapi.ListColumnPredicate;
import cn.remex.db.lambdaapi.ModelColumnPredicate;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.model.Modelable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static cn.remex.db.sql.SqlType.*;
import static cn.remex.db.sql.FieldType.*;

/**
 * Created by yangy on 2016/1/11 0011.
 */

/**
 * T 当前语句主表的类型。
 * CT 当前Column关联的Model的类型
 * ST 当前节点的属性节点的类型
 * S2T 二层子节点的类型
 * */
public class SqlColumn<T extends Modelable, CT extends Modelable, ST extends Modelable> {

    public static <T extends Modelable, CT extends Modelable, ST extends Modelable>
        void obtainFieldColumn(SqlColumn<T, CT, ST> supNode, Object input, Consumer<SqlColumn<T, ST, ?>> sqlColumnConsumer){
        Consumer p = s -> {
            SqlColumn sc = new SqlColumn(supNode).init(s.toString(), null, null, null, TBase);
            if(null!=supNode)supNode.subColumns.add(sc);
            //内部链式访问
            if(null!=sqlColumnConsumer)sqlColumnConsumer.accept(sc);
        };

        Assert.notNull(input, "输入仅接受字符串和ModelColumnPredicate lambda表达式。");
        if (input instanceof ColumnPredicate) {
            ColumnPredicate mcp = (ColumnPredicate) input;
            ReflectUtil.eachFieldWhenGet(supNode.nodeBean, c -> mcp.init((ST) c), p);
        } else {
            p.accept(input);
        }
    }
    public static <T extends Modelable, CT extends Modelable, ST extends Modelable, S2T extends Modelable>
        void obtainModelColumn(SqlColumn<T, CT, ST> supNode, Object input, Consumer<SqlColumn<T, ST, S2T>> sqlColumnConsumer){
        Consumer p = c -> {
            SqlColumn<T, ST, S2T> sc = new SqlColumn<>(supNode);
            Class<S2T> nodeClass = (Class<S2T>) getFields(supNode.nodeClass, TObject).get(c.toString());
            S2T nodeBean = ReflectUtil.createAopBean(nodeClass);
            Assert.notNull(nodeClass, "Model属性不在模型中！");
            sc.init(c.toString(), nodeBean, nodeClass,nodeClass, TObject);
            if(null!=supNode)supNode.subColumns.add(sc);

            //内部链式访问
            if(null!=sqlColumnConsumer)sqlColumnConsumer.accept(sc);
        };

        Assert.notNull(input, "输入仅接受字符串和ModelColumnPredicate lambda表达式。");
        if (input instanceof ModelColumnPredicate) {
            ModelColumnPredicate mcp = (ModelColumnPredicate) input;
            ReflectUtil.eachFieldWhenGet(supNode.nodeBean, c -> mcp.init((ST) c), p);
        } else {
            p.accept(input);
        }

    }
    public static <T extends Modelable, CT extends Modelable, ST extends Modelable, S2T extends Modelable>
        void obtainListColumn(SqlColumn<T, CT, ST> supNode, Object input, Consumer<SqlColumn<T, ST, S2T>> sqlColumnConsumer){
        Consumer p = c -> {
            SqlColumn<T, ST, S2T> sc = new SqlColumn<>(supNode);
            //此处的C是List类型属性的属性名
            Type listClass = SqlType.getFields(supNode.nodeClass, FieldType.TCollection).get(c.toString());
            Class<S2T> nodeClass = (Class<S2T>) ReflectUtil.getListActualType(listClass);
            Assert.notNull(nodeClass, "List属性不在模型中！");
            S2T nodeBean = ReflectUtil.createAopBean(nodeClass);
            sc.init(c.toString(), nodeBean, nodeClass, listClass, FieldType.TCollection);
            if(null!=supNode)supNode.subColumns.add(sc);

            //内部链式访问
            if(null!=sqlColumnConsumer)sqlColumnConsumer.accept(sc);
        };

        Assert.notNull(input, "输入仅接受字符串和ModelColumnPredicate lambda表达式。");
        if (input instanceof ListColumnPredicate) {
            ListColumnPredicate mcp = (ListColumnPredicate) input;
            ReflectUtil.eachFieldWhenGet(supNode.nodeBean, c -> mcp.init((ST) c), p);
        } else {
            p.accept(input);
        }
    }

    private FieldType type;
    /**
     * 当前接的父属性节点。如果是当前表的属性列，则父节点为空。
     */
    private SqlColumn supColumn;
    /**
     * 当前节点属性的类型。如果是基本列，则nodeClass为null；如果是Model列或Collection列则是ModelClass
     */
    private Class<ST> nodeClass;
    /**
     * 当前节点属性的类型，如果是基本列，则nodeClass为null；如果是Model列则是ModelClass；如果是Collection列则是List<ModelClass>
     */
    private Type nodeType;
    private ST nodeBean;
    private DbCvo<T> dbCvo;
    private String fieldName;
    private AggregateFunction aggregateFunction;
    private boolean isGroup = false;

    private List<SqlColumn<T,ST,?>> subColumns = new ArrayList<>();

    //当是面向对象的属性时，则有表名，虚拟表名
    private String tableName;
    private String aliasName;//虚拟表名只能再select实例化并调用去生成sql时才能产生
    private String fieldAliasName;//init时产生，init函数必须调用，否则功能会出错。


    private SqlBeanWhere<T,CT> filters;
    /*构造及初始化函数*/
    public SqlColumn(DbCvo<T> dbCvo, Class<ST> beanClass, ST t) {
        this.dbCvo = dbCvo;
        this.supColumn = null;
        this.type = TROOT;
        this.nodeType = beanClass;
        this.nodeClass = beanClass;
        this.nodeBean = t;

        this.fieldAliasName = null==supColumn?this.fieldName:obtainChain()+this.fieldName;
    }

    public SqlColumn(SqlColumn supColumn) {
        this.dbCvo = supColumn.dbCvo;
        this.supColumn = supColumn;
    }
    public SqlColumn<T, CT, ST> init(String fieldName, ST nodeBean, Class<ST> nodeClass, Type nodeType, FieldType type) {
        this.type = type;
        this.fieldName = fieldName;
        this.nodeType = nodeType;
        this.nodeClass = nodeClass;
        this.nodeBean = nodeBean;

        this.fieldAliasName = null==supColumn?this.fieldName:obtainChain()+this.fieldName;
        return this;
    }


    /*lambda链式访问函数*/
    public StringBuilder obtainChain() {
        if(TROOT.equals(type)) {
            return new StringBuilder();
        }else{
            // [].用于描述list属性下的引用   .用于描述Model属性下的引用
            StringBuilder sb = supColumn.obtainChain();
            if(TBase.equals(type)){
                //;
            }else {
                sb.append(fieldName).append(TCollection.equals(type) ? "[]." : ".");
            }
            return sb;
        }
    }

    public SqlColumn<T, CT, ST> withColumns(ColumnPredicate<ST> cp, Consumer<SqlColumn<T, ST, ?>> sqlColumnConsumer) {
        obtainFieldColumn(this, cp, sqlColumnConsumer);
        return this;
    }

    public SqlColumn<T, CT, ST> withColumns(ColumnPredicate<ST> cp) {
        obtainFieldColumn(this, cp, null);
        return this;
    }

    public SqlColumn<T, CT, ST> withColumns(String fieldName, Consumer<SqlColumn<T, ST, ?>> sqlColumnConsumer) {
        obtainFieldColumn(this, fieldName, sqlColumnConsumer);
        return this;
    }

    public SqlColumn<T, CT, ST> withColumns(String fieldName) {
        obtainFieldColumn(this, fieldName, null);
        return this;
    }
    //添加本节点所有基本TBase列到Sql语句中
    public SqlColumn<T, CT, ST> withColumns() {
        SqlType.getGetters(this.nodeClass,TBase).forEach((k,v)->obtainFieldColumn(this, k, null));
        return this;
    }
    public <S2T extends Modelable>  SqlColumn<T, CT, ST> withModel(ModelColumnPredicate<T, ST, S2T> mcp, Consumer<SqlColumn<T, ST, S2T>> sqlColumnConsumer) {
        obtainModelColumn(this, mcp, sqlColumnConsumer);
        return this;
    }
    public <S2T extends Modelable>  SqlColumn<T, CT, ST> withModel(ModelColumnPredicate<T, ST, S2T> mcp) {
        obtainModelColumn(this, mcp, null);
        return this;
    }
    public <S2T extends Modelable> SqlColumn<T, CT, ST> withModel(String fieldName, Consumer<SqlColumn<T, ST, S2T>> sqlColumnConsumer) {
        obtainModelColumn(this, fieldName, sqlColumnConsumer);
        return this;
    }
    public <S2T extends Modelable> SqlColumn<T, CT, ST> withModel(String fieldName) {
        obtainModelColumn(this, fieldName, null);
        return this;
    }
    public <S2T extends Modelable>  SqlColumn<T, CT, ST> withList(ListColumnPredicate<T, ST, S2T> lcp, Consumer<SqlColumn<T, ST, S2T>> sqlColumnConsumer) {
        obtainListColumn(this, lcp, sqlColumnConsumer);
        return this;
    }
    public <S2T extends Modelable>  SqlColumn<T, CT, ST> withList(ListColumnPredicate<CT, ST, S2T> lcp) {
        obtainListColumn(this, lcp, null);
        return this;
    }
    public SqlColumn<T, CT, ST> filterBy(ColumnPredicate<ST> wp, WhereRuleOper oper, String value) {
        if(null == this.filters) this.filters = new SqlBeanWhere();
        ReflectUtil.eachFieldWhenGet(nodeBean, b -> wp.init((ST) b), s -> this.filters.addRule(s.toString(), oper, value));
        return this;
    }
    public SqlColumn<T, CT, ST> filterBy(Consumer<SqlBeanWhere<T, CT>> filterConsumer) {
        if(null == this.filters) this.filters = new SqlBeanWhere();
       filterConsumer.accept(this.filters);
        return this;
    }
    public SqlColumn<T, CT, ST> groupBy(){
        dbCvo._setHasGroupBy(true);
        setIsGroup(true);
        return this;
    }
    public SqlColumn<T, CT, ST> aggregateBy(AggregateFunction fun){
        dbCvo._setHasGroupBy(true);
        aggregateFunction = fun;
        return this;
    }

    public DbCvo<T> dbCvo() {
        return this.dbCvo;
    }


    /**
     * 遍历每个节点，含子节点
     * @param c
     * @return
     */
    public SqlColumn<T, CT, ST> forEvery(Consumer<SqlColumn> c) {
        c.accept(this);
        if(null!=subColumns)subColumns.forEach(cc->cc.forEvery(c));
        return this;
    }

    public SqlColumn supColumn() {
        return this.supColumn;
    }



    //getter setter
    public SqlColumn getSupColumn() {
        return supColumn;
    }

    public void setSupColumn(SqlColumn supColumn) {
        this.supColumn = supColumn;
    }

    public Class<ST> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<ST> nodeClass) {
        this.nodeClass = nodeClass;
    }

    public ST getNodeBean() {
        return nodeBean;
    }

    public void setNodeBean(ST nodeBean) {
        this.nodeBean = nodeBean;
    }

    public DbCvo<T> getDbCvo() {
        return dbCvo;
    }

    public void setDbCvo(DbCvo<T> dbCvo) {
        this.dbCvo = dbCvo;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public SqlBeanWhere<T,CT> getFilters() {
        return filters;
    }

    public void setFilters(SqlBeanWhere<T,CT> filters) {
        this.filters = filters;
    }
    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getFieldAliasName() {
        return fieldAliasName;
    }

    public void setFieldAliasName(String fieldAliasName) {
        this.fieldAliasName = fieldAliasName;
    }

    public List<SqlColumn<T, ST, ?>> getSubColumns() {
        return subColumns;
    }

    public void setSubColumns(List<SqlColumn<T, ST, ?>> subColumns) {
        this.subColumns = subColumns;
    }

    public Type getNodeType() {
        return nodeType;
    }

    public void setNodeType(Type nodeType) {
        this.nodeType = nodeType;
    }

    public AggregateFunction getAggregateFunction() {
        return aggregateFunction;
    }

    public void setAggregateFunction(AggregateFunction aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

}
