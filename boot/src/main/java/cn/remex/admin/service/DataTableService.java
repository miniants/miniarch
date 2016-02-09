package cn.remex.admin.service;

import cn.remex.admin.appbeans.DataCvo;
import cn.remex.admin.appbeans.DataRvo;
import cn.remex.core.util.JsonHelper;
import cn.remex.core.util.Judgment;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.sql.SqlBeanWhere;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;

/**
 * @auther GQY
 * @Date 2016/1/5.
 */
@BusinessService
public class DataTableService {
    @BusinessService
    public BsRvo list(DataCvo bsCvo,String modelName,String filters,String extColumn) {

        Class clazz = ContainerFactory.getSession().obtainModelClass(modelName);
        DbCvo ret = ContainerFactory.getSession().createDbCvo(clazz)//.orderBy(t->t.getId(),Sort.ASC)
                .orderBy(bsCvo.getSidx(), bsCvo.getSord())
                .rowCount(bsCvo.getRowCount()).page(bsCvo.getPagination())
                ;
        if(!Judgment.nullOrBlank(extColumn)){
            ret.setDataType("bdodoed");
            ret.putExtColumn(extColumn);
        }
        ret = Judgment.nullOrBlank(filters)?ret: ret.filter(JsonHelper.toJavaObject(filters, SqlBeanWhere.class));
        return new DataRvo(true,ret.ready().query());
    }
}
