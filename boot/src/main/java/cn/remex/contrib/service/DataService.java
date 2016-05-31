package cn.remex.contrib.service;

import cn.remex.contrib.appbeans.DataCvo;
import cn.remex.contrib.appbeans.DataRvo;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;

/**
 * @auther GQY
 * @Date 2016/1/5.
 */
@BusinessService
public class DataService {


    @BusinessService
    public BsRvo list(DataCvo bsCvo, String modelName, String filters, String extColumn) {
        Class clazz = ContainerFactory.getSession().obtainModelClass(modelName);
        DbCvo ret = bsCvo.obtainDbCvo(clazz);
        return new DataRvo(ret.ready().query());
    }

    //!!!!!!!!!!!!!!!!!!!!!!警告!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
    //DataService 是一个特殊的用于数据库操作的服务，里面的所有方法均会以/smvc/DataService/methodName/(param)的形式对外暴露。请不要将业务方法或特定的一类方法放到这里。
    //可以参考AuthenticateBtx.obtainDefaultUris();

}
