package cn.remex.db;

import cn.remex.core.Rvo;
import cn.remex.db.appbeans.BeanVo;
import cn.remex.db.appbeans.MapVo;
import cn.remex.db.lambdaapi.ColumnPredicate;
import cn.remex.db.rsql.RsqlDao;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.AggregateFunction;
import cn.remex.db.sql.SqlColumn;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public abstract class DbRvo implements Rvo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2873012626158632379L;
	private StringBuilder msg = new StringBuilder();
	private boolean status;

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setMsg(final boolean status, final String msg) {
		this.status = status;
		this.msg.append(msg);
	}
	public abstract Object getCell(int rowNO, int columnNO);
	public abstract <T extends ModelableImpl> Object getCell(int rowNO, ColumnPredicate<T> cp, AggregateFunction aggregateFunction);

	public abstract Object getCell(int rowIndex, String needColumnName);

	public abstract List<Object> getCells(int columnIndex, String columnValue,
			int needColumnIndex);

	public abstract List<Object> getCells(int columnIndex, String columnValue,
			String needColumnName);

	public abstract List<Object> getCells(String columnName,
			String columnValue, int needColumnIndex);

	public abstract List<Object> getCells(String columnName,
			String columnValue, String needColumnName);
	public abstract <T extends ModelableImpl> List<Object> getCells(final ColumnPredicate<T> cp, final String columnValue, final ColumnPredicate<T> needColumnNameCp, AggregateFunction aggregateFunction);
	public abstract <T extends ModelableImpl> Object getCell(final ColumnPredicate<T> columnCp, final String columnValue, final ColumnPredicate<T> needColumnNameCp, AggregateFunction aggregateFunction);

	public abstract List<Object> getColumn(int index);

	public abstract <T> List<T> getColumn(int index, Class<T> clazz);

	public abstract List<Object> getColumn(String needColumnName);

	public abstract int getEffectRowCount();

	public abstract List<?> getGridData();

	public abstract String getId();

	public abstract TreeMap<String, String> getMapFromColumns(String keyColumn,
			String valueColumn);

	public abstract <T extends ModelableImpl>  TreeMap<String, String> getMapFromColumns(ColumnPredicate<T> keyCp, ColumnPredicate<T> valueCp, AggregateFunction valueAggregateFunction);

	public abstract TreeMap<String, String> getMapFromColumns(String keyColumn,
	                                                          String valueColumn, String restrainColumn, String restrainValue);

	public abstract List<Map<String, Object>> getMapRows();

	/** 设置返回消息	 */
	@Override
	public String getMsg() {
		return this.msg.toString();
	}
	public StringBuilder appendMsg(final String msg) {
		this.msg.append(msg);
		return this.msg;
	}
	public abstract int getPagination();

	public abstract int getRecordCount();

	public abstract int getRecords();

	public abstract int getRowCount();

	public abstract List<List<Object>> getRows();

	public abstract List<List<Object>> getRows(int columnIndex,
			String columnValue);

	public abstract List<List<Object>> getRows(String columnName,
			String columnValue);

	/** 设置数据状态	 */
	@Override
	public boolean getStatus() {
		return this.status;
	}

	public abstract int getTitleIndex(String columnName);

	public abstract List<String> getTitles();

	public abstract Map<String, String> getUserData();

	/**
	 * 将查询结果的第一条记录复制给bean
	 */
	public abstract <T extends Modelable> void assignRow(T bean);
	public abstract <T extends Modelable> T obtainBean();
	public abstract <T extends Modelable> List<T> obtainBeans();
	public abstract <T extends Modelable> List<T> obtainBeans(Class<? extends Modelable> modelClass);

	public abstract List<?> obtainObjects();
	public abstract <T> List<T> obtainObjects(final Class<T> clazz);

	public abstract Map<String, String> obtainMap(final String keyColumn, final String valueColumn);
	public abstract Map<String, String> obtainMap(String keyColumn, String valueColumn, String restrainColumn, String restrainValue);

	public abstract <T> Map<String, T> obtainObjectsMap(final String columnName, final Class<T> clazz);
	public abstract <T> Map<String, T> obtainObjectsMap(final String columnName);
	public abstract <T> BeanVo<T> obtainBeanVo(Class<T> clazz);
	public abstract MapVo obtainMapVo();

	public abstract <T> Class<T> getBeanClass();
}
