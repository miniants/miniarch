package cn.remex.db.rsql.model;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.StringHelper;
import cn.remex.db.DbRvo;
import cn.remex.db.model.SysSerialNumber;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.view.Element;

import javax.persistence.Column;
import java.io.Serializable;

public abstract class ModelableImpl implements Serializable, Modelable, SerialNoGenerator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	/**
//	 * 此函数自动生成数据库表中的主键。<br>
//	 * 注意流水号必须是再末尾。<br>
//	 * {@link ModelableImpl#obtainAbbreviation()}获得主键id的格式。
//	 * 如："%1$s%2$tY%2$tm%2$td%3$09d"中的%3$09d为流水号。
//	 * @param modelableImpl
//	 * @return
//	 */
//	private synchronized static Integer generateId(final ModelableImpl modelableImpl) {
////		Integer curId = ORM_CURMAX_ID.get(modelableImpl.getClass());
////		if (null == curId) {
//			Object curMaxId = SysSerialNumber.queryPrimaryKey(modelableImpl.getClass());
////			if (null == curMaxId) {
////				return 1;
////			} else {
//				MatchResult mr = StringHelper.match(modelableImpl.obtainIdFormat(),
//						"%3\\$.?(\\d{1,3})d$",
//						modelableImpl.getClass()+"配置的主键id自动生成的字符串模板有错，必须包括%3及相关格式作为序列的生成模板,且数字部分应该在最后！如：M%3%1$tY%1$tm%1$td%2$09d$");
//				String idxstr =mr.group(1);
//				int idx = Integer.valueOf(idxstr).intValue();
//				String curIds = curMaxId.toString();
//				return Integer.valueOf(curIds.substring(curIds.length() - idx)) + 1;
////			}
////		}
////		ORM_CURMAX_ID.put(modelableImpl.getClass(), curId + 1);
////		return curId;
//	}

	private StringBuilder _hasModifyFields;
	private DbRvo _dbRvo;
	private int _rowNo;
	
	public void _setDbRvo(DbRvo _dbRvo) {
		this._dbRvo = _dbRvo;
	}
	public void _setRowNo(int _rowNo) {
		this._rowNo = _rowNo;
	}


	private boolean _isAopModelBean=false;
//	private StringBuilder _modifyFieldsDataTypeHasnot_bd=false;//
//	private StringBuilder _modifyFieldsDataTypeHasnot_od=true;
//	private StringBuilder _modifyFieldsDataTypeHasnot_cd=true;
	/**
	 * 用于添加已被修改的属性
	 * @param fields
	 */
	public void _addModifyFileds(String... fields){
		if(null==fields || fields.length==0)
			return;
		if(null==this._hasModifyFields)
			this._hasModifyFields=new StringBuilder();
		for(String field:fields){
			this._hasModifyFields.append(field).append(";");
//			if(_modifyFieldsDataTypeHasnot_bd)_modifyFieldsDataTypeHasnot_bd=SqlType.isTBase(type);
		}
	}
	public String _getModifyFileds(){
		return null==this._hasModifyFields?null:this._hasModifyFields.toString();
	}
	public boolean _isAopModelBean(){
		return _isAopModelBean;
	}
	public boolean _setAopModelBean(){
		return _isAopModelBean=true;
	}
	
	
	private String createOperator;
	private String createOperator_name;
	private String createTime;

	private String dataStatus = RsqlConstants.DS_beanNew;
	@Column(length = 30)
	private String id = null;
	@Column(length = 30)
	private String modifyOperator;
	@Column(length = 30)
	private String modifyOperator_name;
	@Column(length = 30)
	private String modifyTime;
	@Column(length = 50)
	private String name;
	@Column(length = 200)
	private String note;
	//LHY 2015-6-21 数据权限重新设计
	/*private Department ownership;*/
	@Column(length = 10)
	private String ownership_name;

	private int version;
	public ModelableImpl() {
	}
	public ModelableImpl(final String name) {
		this.name = name;
	}
	@Override
	/**
	 * 此函数自动生成数据库表中的主键。<br>
	 * 注意流水号必须是再末尾。<br>
	 * @rmx.summary {@link ModelableImpl#obtainAbbreviation()}获得主键id的格式。
	 * 如："%1$s%2$tY%2$tm%2$td%3$09d"中的%3$09d为流水号。
	 * @param modelableImpl
	 * @return
	 */
	public String generateId() {
//		return String.format(obtainIdFormat(),obtainAbbreviation(), System.currentTimeMillis(), generateId(this));
		return String.format(obtainIdFormat(),
				obtainAbbreviation(), 
				System.currentTimeMillis(), 
				Integer.valueOf(SysSerialNumber.createSerialNumber(this.getClass()).toString()));
	}
	public String createSerialNo(String fieldName){
		return String.format(obtainSerialNoFormat(),
				fieldName,
				System.currentTimeMillis(),
				Integer.valueOf(SysSerialNumber.createSerialNumber(this.getClass(), fieldName).toString()));
	}
	
	@Element(label = "创建者(PK)", editable = false, colModelIndex = 200, hidden = true)
	public String getCreateOperator() {
		return this.createOperator;
	}
	@Element(label = "创建者", editable = false, colModelIndex = 200, width = 90)
	public String getCreateOperator_name() {
		return this.createOperator_name;
	}

	@Element(label = "创建时间", editable = false, colModelIndex = 200, width = 90)
	public String getCreateTime() {
		return this.createTime;
	}

	@Override
	@Element(label = "数据状态", editable = false, colModelIndex = 200, hidden = true)
	public String getDataStatus() {
		return this.dataStatus;
	}

	@Override
	@Element(label = "PK", editable = false, colModelIndex = -10, width = 180)
	public String getId() {
		return this.id;
	}

	@Element(label = "修订者(PK)", editable = false, colModelIndex = 200, hidden = true)
	public String getModifyOperator() {
		return this.modifyOperator;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		else if(null!=obj 
				&& obj.getClass()==getClass()
				&& getId() !=null
				&& getId().equals(((ModelableImpl)obj).getId()))
			return true;
		return false;
	}
	@Element(label = "修订者", editable = false, colModelIndex = 200, width = 90)
	public String getModifyOperator_name() {
		return this.modifyOperator_name;
	}

	@Element(label = "修订时间", editable = false, colModelIndex = 200, width = 90)
	public String getModifyTime() {
		return this.modifyTime;
	}

	@Override
	@Element(label = "名称", colModelIndex = -9, width = 80)
	public String getName() {
		return this.name;
	}
	@Element(label = "备注", colModelIndex = 300, width = 80)
	public String getNote() {
		return note;
	}

/*	@Element(label = "所属部门(PK)", editable = false, colModelIndex = 200, hidden = true)
	public Department getOwnership() {
		return this.ownership;
	}*/

	@Element(label = "所属部门名称", editable = false, colModelIndex = 200, width = 140)
	public String getOwnership_name() {
		return this.ownership_name;
	}

	@Override
	@Element(label = "版本", editable = false, colModelIndex = 200, hidden = true)
	public int getVersion() {
		return this.version;
	}

	@Override
	public String obtainAbbreviation() {
		return StringHelper.getAbbreviation(getClass());
	}

	@Override
	public String obtainIdFormat() {
		return "SN%1$s%2$tY%2$tm%2$td%2$tH%2$tM%2$tS%3$08d";
	}
	@Override
	public String obtainSerialNoFormat() {
		return "SN%1$s%2$tY%2$tm%2$td%3$014d";
	}

	public void setCreateOperator(final String createOperator) {
		this.createOperator = createOperator;
	}

	public void setCreateOperator_name(final String createOperatorName) {
		this.createOperator_name = createOperatorName;
	}

	public void setCreateTime(final String createTime) {
		this.createTime = createTime;
	}

	@Override
	public void setDataStatus(final String dataStatus) {
		this.dataStatus = dataStatus;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	public void setModifyOperator(final String modifyOperator) {
		this.modifyOperator = modifyOperator;
	}

	public void setModifyOperator_name(final String modifyOperatorName) {
		this.modifyOperator_name = modifyOperatorName;
	}

	public void setModifyTime(final String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

/*	public void setOwnership(final Department ownership) {
		this.ownership = ownership;
	}*/

	public void setOwnership_name(final String ownershipName) {
		this.ownership_name = ownershipName;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public void trigger() {
	}
	
	
	/*****Map 功能**********/
	
	//private Set<java.jstl.Map.Entry<String, Object>> _entry;
	/*public int size() {
		throw new RsqlException("该Map-size功能尚未实现!");
	}
	public boolean isEmpty() {
		return true;//所有的bean默认都不为空
	}
	public boolean containsKey(Object key) {
		throw new RsqlException("该Map-containsKey功能尚未实现!");
		}
	public boolean containsValue(Object value) {
		throw new RsqlException("该Map-containsValue功能尚未实现!");
		}*/
	public Object get(String key) {
		Object o = null;
		if(null != ReflectUtil.getGetter(this.getClass(), key)){
			o = ReflectUtil.invokeGetter(key, this);
		}else{
			o = _dbRvo.getCell(_rowNo, key);
		}
		return o;
	}
	/*public Object put(String key, Object value) {
		throw new RsqlException("该Map-put功能尚未实现!");
		}
	public Object remove(Object key) {
		throw new RsqlException("该Map-remove功能尚未实现!");
		}
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new RsqlException("该Map-putAll功能尚未实现!");
		}
	public void clear() {
		throw new RsqlException("该Map-clear功能尚未实现!");
		}
	public Set<String> keySet() {
		throw new RsqlException("该Map-keySet功能尚未实现!");
		}
	public Collection<Object> values() {
		throw new RsqlException("该Map-values功能尚未实现!");
		}
	public Set<java.jstl.Map.Entry<String, Object>> entrySet() {
		throw new RsqlException("该Map-entrySet功能尚未实现!");
		}*/

	
	/**Map功能*/
	
}
