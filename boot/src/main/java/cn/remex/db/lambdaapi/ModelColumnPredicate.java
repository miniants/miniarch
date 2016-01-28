package cn.remex.db.lambdaapi;

import cn.remex.db.rsql.model.Modelable;

/**
 * Created by LIU on 15/11/29.
 */
public interface ModelColumnPredicate<T extends Modelable,NT extends Modelable,ST extends Modelable> {
    ST init(NT t);
}
