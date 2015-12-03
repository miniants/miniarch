package cn.com.qqbx.aitp.aiws.xmlbeans.cashout;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ResCashout")
public class ResCashout implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8456289152150866497L;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
