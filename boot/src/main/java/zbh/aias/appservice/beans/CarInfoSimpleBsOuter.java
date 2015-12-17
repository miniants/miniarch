package zbh.aias.appservice.beans;

import java.io.Serializable;

/**
 * @auther GQY
 * @Date 2015/12/4.
 * 车辆简单信息 用于查询车辆详细使用 输出
 */

public class CarInfoSimpleBsOuter implements Serializable{
    private static final long serialVersionUID = 8553938492045336538L;
    private String licenseNo;                //车牌号码
    private String applicantName;           //投保人姓名
    private String applicantId;             //投保人id
    private String sameOwner;               //是否同一人
    private String insuredName;             //被保人姓名
    private String insuredId;                //被保人id
    private String brandName;                //厂牌型号 或车型名称
    private String frameNo;                  //车架号
    private String engineNo;                 //发动机号
    private String enrollDate;               //初次登记日期,注册日期
    private String transferFlag;             //过户车标志
    private String transferDate;             //转移登记日期- 过户日期
    private String rBCode;                   //车型代码
    private String newVehicleFlag;           //新车标志

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getSameOwner() {
        return sameOwner;
    }

    public void setSameOwner(String sameOwner) {
        this.sameOwner = sameOwner;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getInsuredId() {
        return insuredId;
    }

    public void setInsuredId(String insuredId) {
        this.insuredId = insuredId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getrBCode() {
        return rBCode;
    }

    public void setrBCode(String rBCode) {
        this.rBCode = rBCode;
    }

    public String getNewVehicleFlag() {
        return newVehicleFlag;
    }

    public void setNewVehicleFlag(String newVehicleFlag) {
        this.newVehicleFlag = newVehicleFlag;
    }
}

