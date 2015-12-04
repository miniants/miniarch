package zbh.aias.model.system;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;

public class LoginLog  extends ModelableImpl{
	private static final long serialVersionUID = -7195862773010432538L;
	@Column(length = 20)
	private String loginChannel;//登录渠道
	@Column(length = 20)
	private String loginTime;//登录时间
	@Column(length = 20)
	private String loginSuccFlag;//是否登录成功
	@Column(length = 20)
	private String clientIp;//客户端ip
	@Column(length = 30)
	private String nickname;//昵称
	@Column(length = 30)
	private String username;//用户名
	@Column(length = 100)
	private String logMsg;//登录消息
	
//	private String password;//密码
	
	public String getLogMsg() {
		return logMsg;
	}
	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}
	public String getLoginChannel() {
		return loginChannel;
	}
	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginSuccFlag() {
		return loginSuccFlag;
	}
	public void setLoginSuccFlag(String loginSuccFlag) {
		this.loginSuccFlag = loginSuccFlag;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
