package anbox.aibc.model.system;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class LoginLog  extends ModelableImpl{
	private static final long serialVersionUID = -7195862773010432538L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String loginChannel;//登录渠道
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String loginTime;//登录时间
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String loginSuccFlag;//是否登录成功
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String clientIp;//客户端ip
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String nickname;//昵称
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String username;//用户名
	
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
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
