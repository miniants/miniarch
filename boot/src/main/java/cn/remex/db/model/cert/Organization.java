package cn.remex.db.model.cert;

import campus.comm.models.campusbase.ClassroomInfo;
import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.*;
import java.util.List;

/**
 * @auther GQY
 * @Date 2016/1/6.
 * 组织机构表
 */
@Table(uniqueConstraints={@UniqueConstraint(columnNames="code")})
public class Organization extends ModelableImpl {
    private static final long serialVersionUID = -4376652426959274020L;
    @Column(length = 30)
    private String code; //代号
    @Column(length = 80)
    private String orgName; // 名称
    @Column(length = 50)
    private String shortName; //简称
    @Column(length = 100)
    private String engName; //英文
    @Column(length = 30)
    private String companyCode; //单位代号
//    private String userStatus;  //是否使用.. 2016年3月24日20:39:12
    @Column(length = 40)
    @ManyToOne()
    private Organization parentUnit;//上级单位(组织机构id)
    @OneToMany(mappedBy = "parentUnit")
    private List<Organization> subUnits;
    @OneToMany(mappedBy = "actualManagement")
    private List<ClassroomInfo> classroomInfos;
    @ManyToMany(mappedBy = "organizations")
    private List<AuthRole> manageRoles; //管理角色 2016年1月31日19:57:23

    public List<Organization> getSubUnits() {
        return subUnits;
    }

    public void setSubUnits(List<Organization> subUnits) {
        this.subUnits = subUnits;
    }

    public List<ClassroomInfo> getClassroomInfos() {
        return classroomInfos;
    }

    public void setClassroomInfos(List<ClassroomInfo> classroomInfos) {
        this.classroomInfos = classroomInfos;
    }

    public List<AuthRole> getManageRoles() {
        return manageRoles;
    }

    public void setManageRoles(List<AuthRole> manageRoles) {
        this.manageRoles = manageRoles;
    }

    public Organization getParentUnit() {
        return parentUnit;
    }

    public void setParentUnit(Organization parentUnit) {
        this.parentUnit = parentUnit;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
