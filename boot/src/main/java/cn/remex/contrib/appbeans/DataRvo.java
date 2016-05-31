package cn.remex.contrib.appbeans;

import cn.remex.core.exception.ServiceCode;
import cn.remex.db.DbRvo;
import cn.remex.web.service.BsRvo;

import java.util.List;

/**
 * Created by LIU on 15/12/14.
 */
public class DataRvo extends BsRvo {
    private static final long serialVersionUID = 7623706753751877136L;
    private List<?> datas;

    private int rowCount;
    private int pagination;
    private int pageCount;
    private int recordCount;
    private int effectRowCount;
    private String pk;

    public DataRvo(){
    }
    public DataRvo(DbRvo dbRvo) {
        super(dbRvo.getStatus()? ServiceCode.SUCCESS: ServiceCode.RSQL_FAIL, dbRvo.getMsg());
        this.datas = dbRvo.obtainObjects();
        this.rowCount = dbRvo.getRowCount();
        this.pagination = dbRvo.getPagination() == 0 ? 1 : dbRvo.getPagination();
        this.recordCount = dbRvo.getRecordCount();
        if (dbRvo.getRowCount() != 0) {
            double b = (getRecordCount() * 1.00) / getRowCount();
            this.pageCount = ((int) Math.ceil(b));
        }
        effectRowCount = dbRvo.getEffectRowCount();
        pk = dbRvo.getId();
    }
    public DataRvo(String code, String msg, List<?> datas) {
        super(code, msg);
        this.datas = datas;
        this.rowCount = datas.size();
        this.pagination = 1;
        this.recordCount = rowCount;
        this.pageCount = 1;
    }

    public int getRowCount() {
        return rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    public int getPagination() {
        return pagination;
    }
    public void setPagination(int pagination) {
        this.pagination = pagination;
    }
    public int getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public List<?> getDatas() {
        return datas;
    }
    public void setDatas(List<?> datas) {
        this.datas = datas;
    }

    public int getEffectRowCount() {
        return effectRowCount;
    }

    public void setEffectRowCount(int effectRowCount) {
        this.effectRowCount = effectRowCount;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
