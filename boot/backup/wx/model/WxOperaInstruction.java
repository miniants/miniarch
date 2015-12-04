package zbh.wx.model;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import zbh.remex.db.sql.SqlTypeAnnotation;

public class WxOperaInstruction extends ModelableImpl{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6262515722444441390L;
	@Column(length = 10)
	private String instruction;
	@Column(length = 50)
	private String execution;
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getExecution() {
		return execution;
	}
	public void setExecution(String execution) {
		this.execution = execution;
	};
	
}
