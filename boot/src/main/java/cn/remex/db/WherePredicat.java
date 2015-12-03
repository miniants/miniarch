package cn.remex.db;

import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.model.Modelable;

/**
 * Created by LIU on 15/11/29.
 */
public interface WherePredicat<T extends Modelable> {
    public void init(T t);
}
