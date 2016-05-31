package cn.remex.db.sql;

import cn.remex.core.cache.DataCacheCloneable;
import cn.remex.core.exception.ServiceCode;
import cn.remex.db.exception.RsqlException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Types;

public class NamedParam implements DataCacheCloneable {
	private int index;

	private String name;
	private int type;
	private Object value;
	public NamedParam(final int index, final String name, final int type, final Object value) {
		super();
		this.index = index;
		this.name = name;
		this.type = type;
		this.value = value;
	}

	@Override
	public NamedParam clone() throws CloneNotSupportedException {
		NamedParam cloned = (NamedParam) super.clone();
		cloned.setValue(null);
		return cloned;
	}

	public int getIndex() {
		return this.index;
	}

	public String getName() {
		return this.name;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * 返回命名参数的值。<br>
	 * 此方法不仅仅是value的getter，他还根据Types进行了数据类型的转化
	 * <li>格式化Integer
	 * <li>格式化非一对一类型的object，序列化他
	 * 
	 * @return Object
	 * @throws Exception
	 */
	public Object getValue() {
		if(null == this.value ||this.value.toString().length()==0){
			return null;
		}else if(this.type == Types.CHAR){
			return this.value.toString();
		}else if(this.type == Types.BOOLEAN){
			return String.valueOf(this.value);
		}else if (this.type == Types.INTEGER){
			try {
				return Integer.parseInt(this.value.toString());
			} catch (NumberFormatException e) {
				throw new RsqlException(ServiceCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型应为整型[int]："+this.value);
				//				return null;//never arrived here
			}
		}else if(this.type == Types.FLOAT){
			try {
				return Float.parseFloat(this.value.toString());
			} catch (NumberFormatException e) {
				throw new RsqlException(ServiceCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型应为数字类型[float]："+this.value);
				//				return null;//never arrived here
			}
		}else if(this.type == Types.DOUBLE){
			try {
				return Double.parseDouble(this.value.toString());
			} catch (NumberFormatException e) {
				throw new RsqlException(ServiceCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型应为数字类型[double]："+this.value);
				//				return null;//never arrived here
			}
		}else if(this.type == Types.JAVA_OBJECT){
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(this.value);
				String ret = baos.toString();
				oos.close();
				baos.close();
				return ret;
			} catch (IOException e) {
				throw new RsqlException(ServiceCode.RSQL_DATA_ERROR, "字段【"+this.name+"】数据类型为对象类型[object]，但转化为字节流失败！"+this.value);
				//				return null;//never arrived here
			}
		} else {
			return this.value;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[")
		.append("param=")
		.append(this.index)
		.append(", type=")
		.append(this.type)
		.append(',')
		.append(this.name)
		.append('=')
		.append(this.value)
		.append(']');
		return builder.toString();
	}

	public void setIndex(final int namedParamIndex) {
		this.index = namedParamIndex;
	}

	public void setName(final String namedParamName) {
		this.name = namedParamName;
	}

	public void setType(final int namedParamType) {
		this.type = namedParamType;
	}

	public void setValue(final Object namedParamValue) {
		this.value = namedParamValue;
	}
}

