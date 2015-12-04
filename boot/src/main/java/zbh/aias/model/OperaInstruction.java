package zbh.aias.model;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;

public class OperaInstruction extends ModelableImpl{
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
