package klb.appservice.beans;

import java.io.Serializable;

/**
 * @auther GQY
 * @Date 2015/12/4.
 * 车辆简单信息 用于查询车辆详细使用 输出
 */

public class CarInfoSimpleBsOuter implements Serializable{
    private static final long serialVersionUID = 8553938492045336538L;
    private String carNo;
    private String owner;
    private String ownerId; //车主身份证
    private boolean sameOwner;//是否同一人
    private String otherOwner;// 如果不是同一人，出现，--被保险人姓名
    private String otherOwnerId;//如果不是同一人，出现，--被保险人身份证号
    private String brandModel;// 厂牌型号
    private String vin;//车架号
    private String engineNo;//发动机号
    private String signUpDate;//注册日期
    private boolean transfer;//是否过户车
    private String transferDate;//过户日期

    public boolean isSameOwner() {
        return sameOwner;
    }

    public void setSameOwner(boolean sameOwner) {
        this.sameOwner = sameOwner;
    }

    public String getOtherOwner() {
        return otherOwner;
    }

    public void setOtherOwner(String otherOwner) {
        this.otherOwner = otherOwner;
    }

    public String getOtherOwnerId() {
        return otherOwnerId;
    }

    public void setOtherOwnerId(String otherOwnerId) {
        this.otherOwnerId = otherOwnerId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }
}
