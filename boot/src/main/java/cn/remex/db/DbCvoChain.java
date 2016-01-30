package cn.remex.db;

import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.sql.WhereGroupOp;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.sql.SqlBeanOrder;
import cn.remex.db.sql.SqlBeanWhere;
import cn.remex.db.sql.WhereRuleOper;

import java.util.List;

public abstract class DbCvoChain<T extends Modelable> extends DbCvoBase<T> {

    /************************************************************/
    //链式访问方法
    public DbCvoChain<T> put_searchById(String _searchById) {
        set_searchById(_searchById);
        return this;
    }
    public DbCvoChain<T> putBean(Modelable dbBean) {
        setBean(dbBean);
        return this;
    }

    public DbCvoChain<T> putDataColumns(final String dataColumns) {
        setDataColumns(dataColumns);
        return this;
    }
    public DbCvoChain<T> putDataType(final String dataType) {
        setDataType(dataType);
        return this;
    }

    public DbCvoChain<T> putDeleteByWhere(boolean deleteByWhere) {
        setDeleteByWhere(deleteByWhere);
        return this;
    }
    public DbCvoChain<T> putDoCount(boolean doCount) {
        setDoCount(doCount);
        return this;
    }

    public DbCvoChain<T> putDoPaging(boolean doPaging) {
        setDoPaging(doPaging);
        return this;
    }

    public DbCvoChain<T> putGroupOp(WhereGroupOp groupOp) {
        setGroupOp(groupOp);
        return this;
    }

    public DbCvoChain<T> putId(final String id) {
        setId(id);
        return this;
    }

    public DbCvoChain<T> putOK(final boolean status) {
        setOK(status);
        return this;
    }


    public DbCvoChain<T> putOper(SqlOper oper) {
        setOper(oper);
        return this;
    }

    public DbCvoChain<T> putOrders(final List<SqlBeanOrder> orders) {
        setOrders(orders);
        return this;
    }

    public DbCvoChain<T> putPagination(final int pagination) {
        setPagination(pagination);
        return this;
    }

    public DbCvoChain<T> putPoolName(String poolName) {
        setPoolName(poolName);
        return this;
    }

    public DbCvoChain<T> putRecordCount(int recordCount) {
        setRecordCount(recordCount);
        return this;
    }

    public DbCvoChain<T> putReturnType(String returnType) {
        setReturnType(returnType);
        return this;
    }

    public DbCvoChain<T> putRowCount(final int rowCount) {
        this.setDoPaging(true);
        this.setDoCount(true);
        setRowCount(rowCount);
        return this;
    }

    public DbCvoChain<T> putSearch(final boolean search) {
        setSearch(search);
        return this;
    }

    public DbCvoChain<T> putSortable(final boolean sortable) {
        setSortable(sortable);
        return this;
    }
    public DbCvoChain<T> putSpaceName(final String poolName) {
        setSpaceName(poolName);
        return this;
    }
    public DbCvoChain<T> putSqlBeanWhere(SqlBeanWhere sqlBeanWhere) {
        setFilter(sqlBeanWhere);
        return this;
    }
    public DbCvoChain<T> putSqlString(String sqlString) {
        setSqlString(sqlString);
        return this;
    }
    public DbCvoChain<T> putSubList(String subList) {
        setSubList(subList);
        return this;
    }
    public DbCvoChain<T> putUpdateByWhere(boolean updateByWhere) {
        setUpdateByWhere(updateByWhere);
        return this;
    }
    public DbCvoChain<T> putRule(final String field, final WhereRuleOper ruleOper,	final String value){
        addRule(field, ruleOper, value);
        return this;
    }
    public DbCvoChain<T> putGroup(SqlBeanWhere sqlBeanWhere){
        addGroup(sqlBeanWhere);
        return this;
    }
    public DbCvoChain<T> putOrder(final boolean sortable, final Object sidx,final String sord){
        addOrder(sortable, sidx, sord);
        return this;
    }

}
