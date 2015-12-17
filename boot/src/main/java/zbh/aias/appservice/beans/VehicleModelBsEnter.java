package zbh.aias.appservice.beans;

import cn.remex.web.service.BsCvo;

/**
 * @auther GQY
 * @Date 2015/12/5.
 * 查询车辆型号入参
 */
public class VehicleModelBsEnter extends BsCvo {
    private static final long serialVersionUID = -6195818250035762201L;
    private String brandName;  //厂牌型号 或车型名称
    private String engineNo;    //发动机号

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }
}
