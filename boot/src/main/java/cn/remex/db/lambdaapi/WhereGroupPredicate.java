package cn.remex.db.lambdaapi;

import cn.remex.db.rsql.model.Modelable;

/**
 * Created by LIU on 15/11/29.
 */
public interface WhereGroupPredicate<T extends Modelable> {
    void init(T t);
}
