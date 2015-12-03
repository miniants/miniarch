package anbox.aibc.model;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class OperaInstruction extends ModelableImpl{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6262515722444441390L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String instruction;
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
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
