package cn.remex.db.lambdaapi;

import cn.remex.db.rsql.model.Modelable;

import java.util.List;

/**
 * Created by LIU on 15/11/29.
 */
public interface ListColumnPredicate<T extends Modelable,NT extends Modelable,ST extends Modelable> {
    List<ST> init(NT t);
}
