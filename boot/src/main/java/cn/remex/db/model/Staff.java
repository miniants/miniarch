package cn.remex.db.model;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;
import cn.remex.db.view.Element;

import java.sql.Types;

@DataAccessScope(scope=DataAccessScope.everyone)

public class Staff extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5665090490996041857L;
	// 此部分是个人基础资料
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String birthday;
	private Department department;
	//email
	@Column(type=Types.CHAR, length =50, columnDefinition = " ")
	private String email;
	//雇主
	private Organization employer;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String enterDate;
	@Column(type=Types.CHAR, length = 5, columnDefinition = " ")
	private String gender;
	//业务员登记
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String grade;
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
	private String identificationId;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String laborContractCode;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String laborContractSignDate;
	//雇工
	private Person laborer;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String leaveDate;
	//岗位职务
	@Column(type=Types.CHAR, length = 50, columnDefinition = " ")
	private String position;
	@Column(type=Types.CHAR, length = 50, columnDefinition = " ")
	private String practisingCertificateCode;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String qualificationObtainDate;
	@Column(type=Types.CHAR, length = 50, columnDefinition = " ")
	private String qualificationsCode;
	//员工编号
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String sn;
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
	private String tel;
	@Element(label="出生日期")
	public String getBirthday() {
		return this.birthday;
	}
	@Element(label="部门")
	public Department getDepartment() {
		return this.department;
	}
	@Element(label="邮箱")
	public String getEmail() {
		return this.email;
	}
	public Organization getEmployer() {
		return this.employer;
	}
	@Element(label="入司日期")
	public String getEnterDate() {
		return this.enterDate;
	}
	@Element(label="性别")
	public String getGender() {
		return this.gender;
	}
	@Element(label="职级")
	public String getGrade() {
		return this.grade;
	}
	@Element(label="身份证")
	public String getIdentificationId() {
		return this.identificationId;
	}
	@Element(label="劳动合同号")
	public String getLaborContractCode() {
		return this.laborContractCode;
	}
	@Element(label="劳动合同签订日期")
	public String getLaborContractSignDate() {
		return this.laborContractSignDate;
	}
	public Person getLaborer() {
		return this.laborer;
	}
	@Element(label="离司时间")
	public String getLeaveDate() {
		return this.leaveDate;
	}
	@Element(label="职务")
	public String getPosition() {
		return this.position;
	}
	@Element(label="执业证号")
	public String getPractisingCertificateCode() {
		return this.practisingCertificateCode;
	}
	@Element(label="资质证取得日期")
	public String getQualificationObtainDate() {
		return this.qualificationObtainDate;
	}
	@Element(label="资质证号")
	public String getQualificationsCode() {
		return this.qualificationsCode;
	}
	@Element(label="业务员编码")
	public String getSn() {
		return this.sn;
	}
	@Element(label="手机")
	public String getTel() {
		return this.tel;
	}
	public void setBirthday(final String birthday) {
		this.birthday = birthday;
	}
	public void setDepartment(final Department department) {
		this.department = department;
	}
	public void setEmail(final String email) {
		this.email = email;
	}
	public void setEmployer(final Organization employer) {
		this.employer = employer;
	}
	public void setEnterDate(final String enterDate) {
		this.enterDate = enterDate;
	}
	public void setGender(final String gender) {
		this.gender = gender;
	}
	public void setGrade(final String grade) {
		this.grade = grade;
	}
	public void setIdentificationId(final String identificationId) {
		this.identificationId = identificationId;
	}
	public void setLaborContractCode(final String laborContractCode) {
		this.laborContractCode = laborContractCode;
	}
	public void setLaborContractSignDate(final String laborContractSignDate) {
		this.laborContractSignDate = laborContractSignDate;
	}
	public void setLaborer(final Person laborer) {
		this.laborer = laborer;
	}
	public void setLeaveDate(final String leaveDate) {
		this.leaveDate = leaveDate;
	}
	public void setPosition(final String position) {
		this.position = position;
	}
	public void setPractisingCertificateCode(final String practisingCertificateCode) {
		this.practisingCertificateCode = practisingCertificateCode;
	}
	public void setQualificationObtainDate(final String qualificationObtainDate) {
		this.qualificationObtainDate = qualificationObtainDate;
	}
	public void setQualificationsCode(final String qualificationsCode) {
		this.qualificationsCode = qualificationsCode;
	}
	public void setSn(final String laborerSn) {
		this.sn = laborerSn;
	}
	public void setTel(final String tel) {
		this.tel = tel;
	}




}
