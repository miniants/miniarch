package cn.remex.db.sql;

/**
 * Created by LIU on 15/12/1.
 */

/**
 * 条件查询的过滤规则
 */
public enum WhereRuleOper {
    /**
     * 不以...开始
     */
    bn,
    /**
     * 以...开始
     */
    bw,
    /**
     * 包含
     */
    cn,
    /**
     * 不以...结束
     */
    en,
    /**
     * 等于
     */
    eq,
    /**
     * 以...结束
     */
    ew,
    /**
     * 大于等于
     */
    ge,
    /**
     * 大于
     */
    gt,
    /**
     * 小于等于
     */
    le,
    /**
     * 小于
     */
    lt,
    /**
     * 不包含
     */
    nc,
    /**
     * 不等于
     */
    ne,
    /**
     * 在....里面
     */
    in,
    /**
     * 不在...里面
     */
    ni,
    /**
     * 为空
     */
    isNull,
    inSubSelect, existSubSelect, notInSubSelect, notExistSubSelect, /**
     * 不为空
     */
    notNull
   
}
