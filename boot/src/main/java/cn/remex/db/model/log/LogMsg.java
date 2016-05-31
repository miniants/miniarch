package cn.remex.db.model.log;

import cn.remex.db.rsql.model.ModelableImpl;

public abstract class LogMsg extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2923267653613544662L;
	private String event;
	private String logLevel;
	private String recordTime;
	public LogMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LogMsg(final String name, final String event, final String recordTime) {
		super(name);
		this.event = event;
		this.recordTime = recordTime;
	}
	public String getEvent() {
		return this.event;
	}
	public String getLogLevel() {
		return this.logLevel;
	}
	public String getRecordTime() {
		return this.recordTime;
	}
	public void setDebugLevel(){
		this.logLevel = "DEBUG";
	}
	public void setErrorLevel(){
		this.logLevel = "ERROR";
	}
	public void setEvent(final String event) {
		this.event = event;
	}
	public void setImportLevel(){
		this.logLevel = "  VIP";
	}
	public void setInfoLevel(){
		this.logLevel = " INFO";
	}
	public void setLogLevel(final String logLevel) {
		this.logLevel = logLevel;
	}
	public void setRecordTime(final String recordTime) {
		this.recordTime = recordTime;
	}
	public void setWarnLevel(){
		this.logLevel = " WARN";
	}
}
