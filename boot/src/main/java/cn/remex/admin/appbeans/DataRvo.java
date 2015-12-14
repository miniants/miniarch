package cn.remex.admin.appbeans;

import cn.remex.db.DbRvo;
import cn.remex.web.service.BsRvo;

import java.util.List;

/**
 * Created by LIU on 15/12/14.
 */
public class DataRvo extends BsRvo {

    private List<?> datas;

    private int rowCount;
    private int pagination;
    private int pageCount;
    private int recordCount;

    public DataRvo(){
    }
    public DataRvo(boolean status, DbRvo dbRvo) {
        super(status);
        this.datas = dbRvo.obtainObjects();
        this.rowCount=dbRvo.getRowCount();
        this.pagination=dbRvo.getPagination();
        this.recordCount=dbRvo.getRecordCount();
        if(dbRvo.getRowCount() != 0){
            double b = (getRecordCount()*1.00)/getRowCount();
            this.pageCount = ((int)Math.ceil(b));
        }
    }
    public DataRvo(boolean status, List<?> datas) {
        super(status);
        this.datas = datas;
        this.rowCount=datas.size();
        this.pagination=1;
        this.recordCount=rowCount;
        this.pageCount=1;
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
}
