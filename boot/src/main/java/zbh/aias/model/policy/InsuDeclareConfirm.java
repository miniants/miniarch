package zbh.aias.model.policy;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;
/**
 * 投保声明确认
 * @author guoshaopeng
 * @since  2013-8-25
 */
public class InsuDeclareConfirm extends ModelableImpl {

	private static final long serialVersionUID = -6052609572410507571L;
	@Column(length = 30)
	private String quoteNo;      //询价单号
	@Column(length = 30)
	private String contactName;  //联系人 
	@Column(length = 20)
	private String mobile;       //手机
	@Column(length = 20)
	private String tel;          //固定电话
	@Column(length = 200)
	private String postAddr;     //递送地址
	@Column(length = 50)
	private String email;        //邮箱
	@Column(length = 300)
	private String addedServices;//增值服务，以","隔开
	public String getQuoteNo() {
		return quoteNo;
	}
	public void setQuoteNo(String quoteNo) {
		this.quoteNo = quoteNo;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPostAddr() {
		return postAddr;
	}
	public void setPostAddr(String postAddr) {
		this.postAddr = postAddr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddedServices() {
		return addedServices;
	}
	public void setAddedServices(String addedServices) {
		this.addedServices = addedServices;
	}

}
