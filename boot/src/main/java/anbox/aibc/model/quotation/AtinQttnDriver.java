package anbox.aibc.model.quotation;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 询价单驾驶员信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinQttnDriver extends ModelableImpl {

	private static final long serialVersionUID = 8482802670075389775L;
	private AtinQuotation atinQuotation;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String driver;              //驾驶员姓名
	@SqlTypeAnnotation(type=Types.CHAR, length = 5, sqlType = " ")
	private String sex;                 //驾驶员性别
	private int age;                 //驾驶员年龄
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String marriageFlag;        //婚否
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String drivLicenseNo;       //驾驶证号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String drivVhicleType;      //准驾车型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String licenseDate;         //初次领证日期
	private int drivingAge;          //驾龄

	public AtinQuotation getAtinQuotation() {
		return atinQuotation;
	}
	public void setAtinQuotation(AtinQuotation atinQuotation) {
		this.atinQuotation = atinQuotation;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getMarriageFlag() {
		return marriageFlag;
	}
	public void setMarriageFlag(String marriageFlag) {
		this.marriageFlag = marriageFlag;
	}
	public String getDrivLicenseNo() {
		return drivLicenseNo;
	}
	public void setDrivLicenseNo(String drivLicenseNo) {
		this.drivLicenseNo = drivLicenseNo;
	}
	public String getDrivVhicleType() {
		return drivVhicleType;
	}
	public void setDrivVhicleType(String drivVhicleType) {
		this.drivVhicleType = drivVhicleType;
	}
	public String getLicenseDate() {
		return licenseDate;
	}
	public void setLicenseDate(String licenseDate) {
		this.licenseDate = licenseDate;
	}
	public int getDrivingAge() {
		return drivingAge;
	}
	public void setDrivingAge(int drivingAge) {
		this.drivingAge = drivingAge;
	}
}
