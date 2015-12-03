package cn.remex.db.model.log;

public class LogonLogMsg extends LogMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9090405569758737539L;

	private String ip;
	private String uri;
	private String username;

	public LogonLogMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LogonLogMsg(final String name, final String event, final String recordTime,
			final String username, final String ip, final String uri) {
		super(name, event, recordTime);
		this.username = username;
		this.ip = ip;
		this.uri = uri;
	}
	public String getIp() {
		return this.ip;
	}
	public String getUri() {
		return this.uri;
	}
	public String getUsername() {
		return this.username;
	}
	public void setIp(final String ip) {
		this.ip = ip;
	}
	public void setUri(final String uri) {
		this.uri = uri;
	}
	public void setUsername(final String username) {
		this.username = username;
	}

}
