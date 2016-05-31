package cn.remex.db.rsql.model;

public interface Modelable {




	/**
	 * 用于添加已被修改的属性，并判断是否什么类型的属性，由于后续的优化
	 * @param fields
	 */
	public void _addModifyFileds(String... fields);
	public String _getModifyFileds();
	/**
	 * 默认返回本类是否是cglib代理的数据库modelbean
	 * @return
	 */
	public boolean _isAopModelBean();
	public boolean _setAopModelBean();

	public String _getDataStatus();
	public void _setDataStatus(String dataStatus);

	public String getId();

	public String getName();

	public void setId(String id);

	public void setName(String name);

}
