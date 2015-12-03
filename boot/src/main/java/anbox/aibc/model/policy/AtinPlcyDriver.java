package anbox.aibc.model.policy;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 保单驾驶员信息
 * @author guoshaopeng
 * @since  2013-8-28
 */
public class AtinPlcyDriver extends ModelableImpl{

	private static final long serialVersionUID = -5795186725806876646L;
	private AtinPolicy atinPolicy;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String driver;              //驾驶员姓名
	@SqlTypeAnnotation(type=Types.CHAR, length = 5, sqlType = " ")
	private String sex;                 //驾驶员性别
	private int age;                    //驾驶员年龄
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String marriageFlag;        //婚否
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String drivLicenseNo;       //驾驶证号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String drivVhicleType;      //准驾车型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String licenseDate;         //初次领证日期
	private int drivingAge;             //驾龄

	public AtinPolicy getAtinPolicy() {
		return atinPolicy;
	}
	public void setAtinPolicy(AtinPolicy atinPolicy) {
		this.atinPolicy = atinPolicy;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getDrivingAge() {
		return drivingAge;
	}
	public void setDrivingAge(int drivingAge) {
		this.drivingAge = drivingAge;
	}
}
