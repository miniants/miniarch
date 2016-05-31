package cn.remex.db.sql;

import cn.remex.db.view.EditType;

import javax.persistence.Column;
import java.sql.Types;

/**
 * Created by yangy on 2016/1/22 0022.
 */
public class ColumnType {
	//以下属性为Column参数属性
	public int length = 50;
	public int type = Types.CHAR;
	public int scale = 2;

	//以下属性为试图参数
	public EditType editType;
	public Class<?> codeRefBean;
	public String codeRefTypeColumn;
	public String codeRefCodeColumn;
	public String codeRefDescColumn;
	public String codeRefCodeType;
	public String codeRefFilters;

	/**
	 * @param type @see {@link Types}
	 */
	public ColumnType(int type, int length) {
		this.type = type;
		this.length = length;
	}
	public ColumnType(int type, int length, int scale) {
		this.type = type;
		this.length = length;
		this.scale = scale;
	}


	public ColumnType(int type, Column sta) {
		this.length = sta.length();//长度
		if("CLOB".equals(sta.columnDefinition()))
			this.type = Types.CLOB;
		else
			this.type = type;//Types下的变量，java定义的数据字段类型
	}

}
