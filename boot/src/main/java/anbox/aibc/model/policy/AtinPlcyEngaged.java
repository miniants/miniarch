package anbox.aibc.model.policy;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 保单特别约定信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinPlcyEngaged extends ModelableImpl{

	private static final long serialVersionUID = -144150743818308635L;
	private AtinPolicy atinPolicy;
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String prdtKindCode;     //险种编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String code;             //特约代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String serialNo;         //特约序列号
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String lineNo;           //特约行号
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String titleFlag;        //是否为特约头信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String name;             //特约名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 500, sqlType = " ")
	private String content;          //特约内容

	public AtinPolicy getAtinPolicy() {
		return atinPolicy;
	}
	public void setAtinPolicy(AtinPolicy atinPolicy) {
		this.atinPolicy = atinPolicy;
	}
	public String getPrdtKindCode() {
		return prdtKindCode;
	}
	public void setPrdtKindCode(String prdtKindCode) {
		this.prdtKindCode = prdtKindCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}
	public String getTitleFlag() {
		return titleFlag;
	}
	public void setTitleFlag(String titleFlag) {
		this.titleFlag = titleFlag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
