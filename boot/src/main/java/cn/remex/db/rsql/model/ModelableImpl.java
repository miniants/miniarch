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
	private static final long serialVersionUID = 1L;

    //框架内部使用
	private StringBuilder _hasModifyFields;
	private DbRvo _dbRvo;
	private int _rowNo;
    private String _dataStatus = RsqlConstants.DS_beanNew;
	private boolean _isAopModelBean=false;

	public void _setDbRvo(DbRvo _dbRvo) {
		this._dbRvo = _dbRvo;
	}
	public void _setRowNo(int _rowNo) {
		this._rowNo = _rowNo;
	}
	/**
	 * 用于添加已被修改的属性
	 * @param fields
	 */
	@Override
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
	@Override
	public String _getModifyFileds(){
		return null==this._hasModifyFields?null:this._hasModifyFields.toString();
	}
	@Override
	public boolean _isAopModelBean(){
		return _isAopModelBean;
	}
	@Override
	public boolean _setAopModelBean(){
		return _isAopModelBean=true;
	}
	@Override
	public void _setDataStatus(final String dataStatus) {
		this._dataStatus = dataStatus;
	}
	@Override
	public String _getDataStatus() {
		return this._dataStatus;
	}

    //end for 框架内部使用

    /**
     * 此函数自动生成数据库表中的主键。<br>
     * 注意流水号必须是再末尾。<br>
     * @rmx.summary {@link ModelableImpl#obtainAbbreviation()}获得主键id的格式。
     * 如："%1$s%2$tY%2$tm%2$td%3$09d"中的%3$09d为流水号。
     * @return
     */
    @Override
    public String generateId() {
//		return String.format(obtainIdFormat(),obtainAbbreviation(), System.currentTimeMillis(), generateId(this));
        return String.format(obtainIdFormat(),
                obtainAbbreviation(),
                System.currentTimeMillis(),
                Integer.valueOf(SysSerialNumber.createSerialNumber(this.getClass()).toString()));
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
    @Override
    public Object get(String key) {
        Object o = null;
        if(null != ReflectUtil.getGetter(this.getClass(), key)){
            o = ReflectUtil.invokeGetter(key, this);
        }else{
            o = _dbRvo.getCell(_rowNo, key);
        }
        return o;
    }

	//系统字段
	@Column(length = 30)
    @Element(label = "PK", editable = false, colModelIndex = -10, width = 180)
	private String id = null;
	@Column(length = 30)
    @Element(label = "创建者", editable = false, colModelIndex = 200, hidden = true)
	private String createOperator;
	@Column(length = 30)
    @Element(label = "创建时间", editable = false, colModelIndex = 200, width = 90)
	private String createTime;
	@Column(length = 30)
    @Element(label = "修订者", editable = false, colModelIndex = 200, hidden = true)
	private String modifyOperator;
	@Column(length = 30)
    @Element(label = "修订时间", editable = false, colModelIndex = 200, width = 90)
	private String modifyTime;
	@Column(length = 30)
    @Element(label = "所属", editable = false, colModelIndex = 200, width = 90)
	private String ownership;

	//通用字段
	@Column(length = 50)
    @Element(label = "名称", colModelIndex = -9, width = 80)
    private String name;
	@Column(length = 200)
    @Element(label = "备注", colModelIndex = 300, width = 80)
	private String note;

	public ModelableImpl() {
	}
	public ModelableImpl(final String name) {
		this.name = name;
	}

    @Override
    public String getId() {
        return this.id;
    }
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }


	public String getCreateOperator() {
		return this.createOperator;
	}
	public String getCreateTime() {
		return this.createTime;
	}
	public String getModifyOperator() {
		return this.modifyOperator;
	}
	public String getModifyTime() {
		return this.modifyTime;
	}
    public String getOwnership() {
        return ownership;
    }
	public void setCreateOperator(final String createOperator) {
		this.createOperator = createOperator;
	}
	public void setCreateTime(final String createTime) {
		this.createTime = createTime;
	}
	public void setModifyOperator(final String modifyOperator) {
		this.modifyOperator = modifyOperator;
	}
	public void setModifyTime(final String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}

}




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