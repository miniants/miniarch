package zbh.aias.appservice.beans;

import cn.remex.web.service.BsCvo;

/**
 * @auther GQY
 * @Date 2015/12/8.
 */
public class AcceptOrderBsEnter extends BsCvo {
    private static final long serialVersionUID = 8230189189944644714L;
    private String insuCom;
    private String sumPremium;
    private String gift;
    private String fuelCard;
    private String recipients;// 收件人
    private String address; // 收件人地址
    private String phone; // 收件人手机

    @Override
    public String getInsuCom() {
        return insuCom;
    }

    @Override
    public void setInsuCom(String insuCom) {
        this.insuCom = insuCom;
    }

    public String getSumPremium() {
        return sumPremium;
    }

    public void setSumPremium(String sumPremium) {
        this.sumPremium = sumPremium;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getFuelCard() {
        return fuelCard;
    }

    public void setFuelCard(String fuelCard) {
        this.fuelCard = fuelCard;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
