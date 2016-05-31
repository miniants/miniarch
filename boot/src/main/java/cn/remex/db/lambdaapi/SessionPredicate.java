package cn.remex.db.lambdaapi;

import cn.remex.db.DbCvo;
import cn.remex.db.rsql.model.Modelable;

/**
 * Created by LIU on 15/11/29.
 */
public interface SessionPredicate<T extends Modelable> {
    void initDbCvo(DbCvo<T> dbCvo, T t);
}
