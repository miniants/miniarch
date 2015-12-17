package zbh.aias.appservice.beans;

import java.io.Serializable;

/**
 * @auther GQY
 * @Date 2015/12/7.
 * 保险公司报价
 */
public class QuotationBsOuter implements Serializable{
    private static final long serialVersionUID = 7150719176533897988L;
    private String insuCom; // 保险公司代码
    private String insuName; // 保险公司名字
//    private String vciPremium; //商业险保费
    private String tciPremium;// 交强险
    private String sumPremium; // 总保费
    private String sumTravelTax; //车船税
    private String aPremium; // 车损险保费
    private String bPremium; //三者保费
    private String g1Premium; // 盗抢
    private String d3Premium; // 司机
    private String d4Premium; // 乘客
    private String fPremium; //玻璃
    private String x1Premium; //发动机
    private String lPremium; //划痕
    private String zPremium;//自燃
    private String bAmount;//第三者保额
    private String d3Amount;//司机保额
    private String d4Amount;//乘客保额
    private String fModel;//玻璃-型号
    private String lAmount;//划痕保额
    private String aBMPremium;//第三者不计免赔保费
    private String d3MPremium;//司机不计免赔保费
    private String d4MPremium;//乘客不计免赔保费
    private String g1MPremium;//盗抢不计免赔保费

    public String getInsuCom() {
        return insuCom;
    }

    public void setInsuCom(String insuCom) {
        this.insuCom = insuCom;
    }

    public String getInsuName() {
        return insuName;
    }

    public void setInsuName(String insuName) {
        this.insuName = insuName;
    }

    public String getTciPremium() {
        return tciPremium;
    }

    public void setTciPremium(String tciPremium) {
        this.tciPremium = tciPremium;
    }

    public String getSumPremium() {
        return sumPremium;
    }

    public void setSumPremium(String sumPremium) {
        this.sumPremium = sumPremium;
    }

    public String getSumTravelTax() {
        return sumTravelTax;
    }

    public void setSumTravelTax(String sumTravelTax) {
        this.sumTravelTax = sumTravelTax;
    }

    public String getaPremium() {
        return aPremium;
    }

    public void setaPremium(String aPremium) {
        this.aPremium = aPremium;
    }

    public String getbPremium() {
        return bPremium;
    }

    public void setbPremium(String bPremium) {
        this.bPremium = bPremium;
    }

    public String getG1Premium() {
        return g1Premium;
    }

    public void setG1Premium(String g1Premium) {
        this.g1Premium = g1Premium;
    }

    public String getD3Premium() {
        return d3Premium;
    }

    public void setD3Premium(String d3Premium) {
        this.d3Premium = d3Premium;
    }

    public String getD4Premium() {
        return d4Premium;
    }

    public void setD4Premium(String d4Premium) {
        this.d4Premium = d4Premium;
    }

    public String getfPremium() {
        return fPremium;
    }

    public void setfPremium(String fPremium) {
        this.fPremium = fPremium;
    }

    public String getX1Premium() {
        return x1Premium;
    }

    public void setX1Premium(String x1Premium) {
        this.x1Premium = x1Premium;
    }

    public String getlPremium() {
        return lPremium;
    }

    public void setlPremium(String lPremium) {
        this.lPremium = lPremium;
    }

    public String getzPremium() {
        return zPremium;
    }

    public void setzPremium(String zPremium) {
        this.zPremium = zPremium;
    }

    public String getbAmount() {
        return bAmount;
    }

    public void setbAmount(String bAmount) {
        this.bAmount = bAmount;
    }

    public String getD3Amount() {
        return d3Amount;
    }

    public void setD3Amount(String d3Amount) {
        this.d3Amount = d3Amount;
    }

    public String getD4Amount() {
        return d4Amount;
    }

    public void setD4Amount(String d4Amount) {
        this.d4Amount = d4Amount;
    }

    public String getfModel() {
        return fModel;
    }

    public void setfModel(String fModel) {
        this.fModel = fModel;
    }

    public String getlAmount() {
        return lAmount;
    }

    public void setlAmount(String lAmount) {
        this.lAmount = lAmount;
    }

    public String getaBMPremium() {
        return aBMPremium;
    }

    public void setaBMPremium(String aBMPremium) {
        this.aBMPremium = aBMPremium;
    }

    public String getD3MPremium() {
        return d3MPremium;
    }

    public void setD3MPremium(String d3MPremium) {
        this.d3MPremium = d3MPremium;
    }

    public String getD4MPremium() {
        return d4MPremium;
    }

    public void setD4MPremium(String d4MPremium) {
        this.d4MPremium = d4MPremium;
    }

    public String getG1MPremium() {
        return g1MPremium;
    }

    public void setG1MPremium(String g1MPremium) {
        this.g1MPremium = g1MPremium;
    }
}
