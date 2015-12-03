package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Person extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1818038952617490497L;


	// 此部分是个人基础资料
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String birthday;
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String birthplace;


	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String familyAddress;
	// 此部分是个人血缘信息、家庭信息
	private Person father;
	@Column(type=Types.CHAR, length = 5, columnDefinition = " ")
	private String gender;
	@Column(type=Types.CHAR, length = 50, columnDefinition = " ")
	private String graduateSchool;
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
	private String identificationId;
	private boolean isAloneChild;
	private Person mother;
	@Column(type=Types.CHAR, length = 50, columnDefinition = " ")
	private String nativePlace;
	@Column(type=Types.CHAR, length = 10, columnDefinition = " ")
	private String race;
	private Person spouse;



	public String getBirthday() {
		return this.birthday;
	}
	public String getBirthplace() {
		return this.birthplace;
	}
	public String getFamilyAddress() {
		return this.familyAddress;
	}
	public Person getFather() {
		return this.father;
	}
	public String getGender() {
		return this.gender;
	}
	public String getGraduateSchool() {
		return this.graduateSchool;
	}
	public String getIdentificationId() {
		return this.identificationId;
	}
	public Person getMother() {
		return this.mother;
	}
	public String getNativePlace() {
		return this.nativePlace;
	}
	public String getRace() {
		return this.race;
	}
	public Person getSpouse() {
		return this.spouse;
	}
	@Element(label="是否独生子女",edittype=EditType.checkbox)
	public boolean isAloneChild() {
		return this.isAloneChild;
	}
	public void setAloneChild(final boolean isAloneChild) {
		this.isAloneChild = isAloneChild;
	}
	public void setBirthday(final String birthday) {
		this.birthday = birthday;
	}
	public void setBirthplace(final String birthplace) {
		this.birthplace = birthplace;
	}
	public void setFamilyAddress(final String familyAddress) {
		this.familyAddress = familyAddress;
	}
	public void setFather(final Person father) {
		this.father = father;
	}
	public void setGender(final String gender) {
		this.gender = gender;
	}
	public void setGraduateSchool(final String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}
	public void setIdentificationId(final String identificationId) {
		this.identificationId = identificationId;
	}
	public void setMother(final Person mother) {
		this.mother = mother;
	}
	public void setNativePlace(final String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public void setRace(final String race) {
		this.race = race;
	}
	public void setSpouse(final Person spouse) {
		this.spouse = spouse;
	}
}