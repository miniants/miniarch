package anbox.aibc.model.policy;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 保单状态日志
 * @author guoshaopeng
 * @since  2013-9-11
 */
public class PolicyStatusLog extends ModelableImpl {

	private static final long serialVersionUID = -9173074032235168379L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String atinPlcyId;       //内部保单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String statusType;       //状态类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String origValue;        //状态原值
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String origOper;         //上次修改人
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String origDate;         //原修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String mdfyValue;        //修改后值
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String mdfyOper;         //本次修改人
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String mdfyDate;         //修改时间
	public String getAtinPlcyId() {
		return atinPlcyId;
	}
	public void setAtinPlcyId(String atinPlcyId) {
		this.atinPlcyId = atinPlcyId;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public String getOrigValue() {
		return origValue;
	}
	public void setOrigValue(String origValue) {
		this.origValue = origValue;
	}
	public String getOrigOper() {
		return origOper;
	}
	public void setOrigOper(String origOper) {
		this.origOper = origOper;
	}
	public String getOrigDate() {
		return origDate;
	}
	public void setOrigDate(String origDate) {
		this.origDate = origDate;
	}
	public String getMdfyValue() {
		return mdfyValue;
	}
	public void setMdfyValue(String mdfyValue) {
		this.mdfyValue = mdfyValue;
	}
	public String getMdfyOper() {
		return mdfyOper;
	}
	public void setMdfyOper(String mdfyOper) {
		this.mdfyOper = mdfyOper;
	}
	public String getMdfyDate() {
		return mdfyDate;
	}
	public void setMdfyDate(String mdfyDate) {
		this.mdfyDate = mdfyDate;
	}
}
