package zbh.aias.model.policy;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;
/**
 * 保单特别约定信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinPlcyEngaged extends ModelableImpl{

	private static final long serialVersionUID = -144150743818308635L;
	private AtinPolicy atinPolicy;
	@Column(length = 10)
	private String prdtKindCode;     //险种编码
	@Column(length = 30)
	private String code;             //特约代码
	@Column(length = 30)
	private String serialNo;         //特约序列号
	@Column(length = 10)
	private String lineNo;           //特约行号
	@Column(length = 10)
	private String titleFlag;        //是否为特约头信息
	@Column(length = 200)
	private String name;             //特约名称
	@Column(length = 500)
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
