package cn.remex.db.rsql.sqlutil;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.StringHelper;
import cn.remex.db.DbCvo;
import cn.remex.db.exception.RsqlInitException;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlUtils;
import cn.remex.db.rsql.connection.dialect.Dialect;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.*;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yangy on 2016/1/12 0012.
 */
public class Select<T extends Modelable> {
    private Dialect dialect;
    private DbCvo<T> dbCvo;
    private String beanName;
    private String tableName;
    private String aliasName = "T";

    private Index tableIndex;// 用于控制数据库虚拟表明的序号。
    private Index paramIndex;// 用于控制数据库参数的序号。



    //存储相应的变量
    public ArrayList<SqlBeanNamedParam> namedParams = new ArrayList<SqlBeanNamedParam>();
    // 定义对象类型数据的查询sql
    public String prefix = "SELECT \r\n\t";
    public String part_from;// = "\r\nFROM "+dialect.quoteKey(beanName)+" "+tableAliasName;
    public String part_where;// = "\r\n  " + cvo.getFilter().toSQL(tableAliasName, namedParams, 0);
    // 动态排序必须放到dao
    // String part_order = "\r\n"+obtainSQLOrder(Option.getOrder());
    public StringBuilder part_column_sb = new StringBuilder();
    public StringBuilder part_jion_sb = new StringBuilder();
    public StringBuilder part_groupby_sb = new StringBuilder("GROUP BY \r\n\t");
    public StringBuilder sqlString = new StringBuilder();


    public Select(Dialect dialect,DbCvo<T> dbCvo,Index tableIndex,Index paramIndex) {
        this.dialect = dialect;
        this.dbCvo = dbCvo;
        this.tableIndex = tableIndex;
        this.paramIndex = paramIndex;
        this.beanName = this.dbCvo.getBeanName();
        this.tableName = this.dbCvo.getBeanName();
        part_from = "\r\nFROM "+dialect.aliasTableName(tableName,aliasName);
        part_where = "\r\n  " + dbCvo.getFilter().toSQL(true,aliasName, namedParams, paramIndex);
    }

    /**
     * 向select中添加需要查询本表的基本列;
     *
     * 如果dataColumns完全放弃后，这个方法将被{@link #appendColumn(SqlColumn)}替代
     */
    @Deprecated
    public void appendBaseColumn(String fieldName,String fieldAlias){
        part_column_sb.append(dialect.aliasFullName(aliasName, fieldName, fieldAlias)).append(",\r\n\t");
    }

    public void appendColumn(SqlColumn column){
        String curAliasName = null==column.getSupColumn() || null == column.getSupColumn().getFieldAliasName()?aliasName:column.getSupColumn().getAliasName();
        if(dbCvo._isHasGroupBy() && column.getAggregateFunction()!=null){
            part_column_sb.append(dialect.aliasAggrFun(useAggregateFunction(curAliasName, column.getAggregateFunction(), column.getFieldName()), column.getFieldAliasName())).append(",\r\n\t");
        }else{
            part_column_sb.append(dialect.aliasFullName(curAliasName, column.getFieldName(), column.getFieldAliasName())).append(",\r\n\t");
        }
    }

    public void leftJoinModel(SqlColumn column, Index tableIndex, Index paramIndex) {
        Class fieldClass = column.getNodeClass();
        String jioned_Tb = StringHelper.getClassSimpleName(fieldClass);
        String jioned_Tb_alias = StringHelper.getAbbreviation(fieldClass) + tableIndex.index++;

        column.setTableName(jioned_Tb);
        column.setAliasName(jioned_Tb_alias);//虚拟表名只能再select实例化并调用去生成sql时才能产生

        part_jion_sb.append("\r\nLEFT JOIN ").append(dialect.aliasTableName(jioned_Tb, jioned_Tb_alias))
                .append("\r\n\tON ")
                .append(dialect.quoteFullName(jioned_Tb_alias, RsqlConstants.SYS_id))
                .append("=")
                .append(dialect.quoteFullName((null==column.getSupColumn() || null == column.getSupColumn().getFieldAliasName()?aliasName:column.getSupColumn().getAliasName()), column.getFieldName()))
                .append(" ")
        ;
        if(null!=column.getFilters())
            part_where += ((" AND ") + (column.getFilters().toSQL(false, column.getAliasName(), namedParams, paramIndex)));


        //系统关键字段默认加载
        if(!dbCvo._isHasGroupBy())
            part_column_sb.append(dialect.aliasFullName(jioned_Tb_alias, RsqlConstants.SYS_id, column.obtainChain().toString()+RsqlConstants.SYS_id)).append(",\r\n\t");
    }

    public void leftJoinList(SqlColumn column, Index tableIndex, Index paramIndex) {
        Class fieldClass = column.getNodeClass();
        String jioned_Tb = StringHelper.getClassSimpleName(fieldClass);
        String jioned_Tb_alias = StringHelper.getAbbreviation(fieldClass) + tableIndex.index++;

        column.setTableName(jioned_Tb);
        column.setAliasName(jioned_Tb_alias);//虚拟表名只能再select实例化并调用去生成sql时才能产生


        //这里要考虑OneToMany / ManyToMany 的不同情况；OneToMany无论是否级联关系都是在多方维护；ManyToMany无论是否级联关系都是在中间表中维护
        Class parentClass = column.getSupColumn()==null?dbCvo.getBeanClass():(Class) column.getSupColumn().getNodeClass();
        String parentAlias = column.getSupColumn()==null || Judgment.nullOrBlank(column.getSupColumn().getAliasName())?aliasName:column.getSupColumn().getAliasName();
        OneToMany otm = ReflectUtil.getAnnotation(parentClass, column.getFieldName(), OneToMany.class);
        ManyToMany curMtm = ReflectUtil.getAnnotation(parentClass, column.getFieldName(), ManyToMany.class);
        if(otm !=null){
            part_jion_sb.append("\r\nLEFT JOIN ").append(dialect.aliasTableName(jioned_Tb, jioned_Tb_alias))
                    .append("\r\n\tON ")
                    .append(dialect.quoteFullName(jioned_Tb_alias, otm.mappedBy()))
                    .append("=")
                    .append(dialect.quoteFullName((null == column.getSupColumn() || null == column.getSupColumn().getFieldAliasName() ? aliasName : column.getSupColumn().getAliasName()), RsqlConstants.SYS_id))
                    .append(" ")
            ;

        }else if(curMtm !=null){
            String primaryBeanName = parentClass.getSimpleName();//主方类名，默认为当前，根据多多关系检查后，修改
            Class<?> primaryBeanClass = parentClass;//主方类，默认为当前，根据多多关系检查后，修改
            String primaryFieldName = column.getFieldName();//主方多对多属性名，默认为当前，根据多多关系检查后，修改
            Type primaryFieldType = column.getNodeType();//主方多对多属性类型，默认为当前，根据多多关系检查后，修改
            Class<?> tgtBeanClass = ReflectUtil.getListActualType(column.getNodeType());

            /**********************判断是多对多还是一对多***********************************/
            // 如果是多对多，不是一对多，则检查多对多关系
            boolean meIsManyToManyPrimary = true;
            String tgtMappedBy = null,mappedBy=null;//多对多中对方属性名称
            // 对方表的集合字段
            Map<String, Method> tcGetters = SqlType.getGetters(tgtBeanClass, FieldType.TCollection);

            // mb必须主从双方都配置，主方还需配置targetEntity。从方无需配置targetEntity属性。
            mappedBy = curMtm.mappedBy();

            Assert.notNull(mappedBy, new StringBuilder("显式指定为双方维护的多对多映射中本类").append(dbCvo.getBeanClass().getName()).append("没有指定对方类多对多属性:").append(mappedBy).toString(), RsqlInitException.class);
            Assert.isTrue(tcGetters.containsKey(mappedBy), new StringBuilder(" 显式指定为双方维护的多对多映射中对方类").append(tgtBeanClass.getName()).append("不存在该多对多属性:").append(mappedBy).toString(), RsqlInitException.class);

            // 获取对方类多对多声明
            ManyToMany tgtMtm = ReflectUtil.getAnnotation(tgtBeanClass, mappedBy, ManyToMany.class);
            Assert.notNull(tgtMtm,  "显示声明多对多时，需要双方指定ManyToMany声明，此处对方类的ManyToMany声明为空！请更正！", RsqlInitException.class);
            tgtMappedBy = tgtMtm.mappedBy();// 对方类的多对多属性

            Assert.notNull(tgtMappedBy, new StringBuilder("显式指定为双方维护的多对多映射中对方类").append(tgtBeanClass.getName()).append("没有指定本类的多对多属性:").append(mappedBy).toString(), RsqlInitException.class);

            Class<?> curTe = curMtm.targetEntity(); // 检查维护关系 本类中的targetEntity应该等于对方类型
            Class<?> tgtTe = tgtMtm.targetEntity(); // 检查维护关系 对方类中的targetEntity应该等于本类型

            Assert.isTrue((null == curTe && null == tgtTe) || (null != curTe && null != tgtTe), new StringBuilder("显式指定为双方维护的多对多映射中，ManyToMany声明不能同时设置targetEntity或者targetEntity同时为空！设置targetEntity的为主维护方。").append(mappedBy).toString(),
                    RsqlInitException.class);

            meIsManyToManyPrimary = ! "void".equals(curTe.toString());//targetEntity不为空的一方为主维护方


            //如果不是主表，则修改对应的表名和字段。
            if(!meIsManyToManyPrimary){
                primaryBeanName = tgtBeanClass.getSimpleName();
                primaryBeanClass = tgtBeanClass;
                primaryFieldName = mappedBy;//自己mappedby的值刚好是对方的属性
                primaryFieldType = SqlType.getFields(primaryBeanClass, FieldType.TCollection).get(primaryFieldName);
            }
            // 多对多 end

            Map<String, ColumnType> ctColumns = RsqlUtils.obtainSKeyCollectionTableColumns(primaryBeanName, primaryFieldName, primaryFieldType);
            Iterator<Map.Entry<String, ColumnType>> ctkeys = ctColumns.entrySet().iterator();
            String Mid_Foreign_column = ctkeys.next().getKey();
            String Mid_Primary_column = ctkeys.next().getKey();

            String Mid_table_name = RsqlUtils.obtainSKeyCollectionTableName(dialect,primaryBeanName,primaryFieldName);
            String Mid_table_alias = StringHelper.getAbbreviation(parentClass)+"_"+StringHelper.getAbbreviation(tgtBeanClass)+"_"+tableIndex.index++;
            part_jion_sb
                    .append("\n\rLEFT JOIN ").append(dialect.aliasTableName(Mid_table_name, Mid_table_alias))
                    .append("\n\r\tON ")
                    .append(dialect.quoteFullName(Mid_table_alias, Mid_Primary_column)).append("=")
                    .append(dialect.quoteFullName(meIsManyToManyPrimary?parentAlias:jioned_Tb_alias, RsqlConstants.SYS_id))
                    .append("\n\rLEFT JOIN ").append(dialect.aliasTableName(jioned_Tb, jioned_Tb_alias))
                    .append("\n\r\tON ")
                    .append(dialect.quoteFullName(Mid_table_alias, Mid_Foreign_column)).append("=")
                    .append(dialect.quoteFullName(meIsManyToManyPrimary?jioned_Tb_alias:parentAlias, RsqlConstants.SYS_id))
            ;
        }



        if(null!=column.getFilters())
            part_where += ((" AND ") + (column.getFilters().toSQL(false, column.getAliasName(), namedParams, paramIndex)));

        //系统关键字段默认加载
        if(!dbCvo._isHasGroupBy())
            part_column_sb.append(dialect.aliasFullName(jioned_Tb_alias, RsqlConstants.SYS_id, column.obtainChain().toString()+RsqlConstants.SYS_id)).append(",\r\n\t");
    }
    public String sqlString(){
        // 代码与代码名称映射功能添加。
        RsqlUtils.dealWithCodeRef(dbCvo.getBeanClass(), aliasName, part_column_sb);


        String part_column = part_column_sb.delete(part_column_sb.length() - 4,part_column_sb.length()).toString();
        String part_groupBy = dbCvo._isHasGroupBy()?part_groupby_sb.delete(part_groupby_sb.length() - 4,part_groupby_sb.length()).toString():"";

        sqlString.append(prefix).append(part_column).append(part_from).append(
                part_jion_sb.toString()).append(part_where).append(part_groupBy)
        // .append(part_order)
        ;

        // 删除多余空间
        namedParams.trimToSize();

        return sqlString.toString();
    }

    public void groupBy(SqlColumn column) {
        String curAliasName = null == column.getSupColumn() || null == column.getSupColumn().getFieldAliasName()?aliasName:column.getSupColumn().getAliasName();
        part_groupby_sb.append(curAliasName).append(".").append(dialect.openQuote()).append(column.getFieldName()).append(dialect.closeQuote()).append(",\r\n\t");
    }


    private String useAggregateFunction(String curAliasName, AggregateFunction fun, String fieldName) {
        return fun.toString()+"(" +curAliasName+"."+dialect.openQuote()+fieldName+dialect.closeQuote()+")";
    }
}
