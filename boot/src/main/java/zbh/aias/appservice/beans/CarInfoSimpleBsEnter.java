package zbh.aias.appservice.beans;

import cn.remex.web.service.BsCvo;

/**
 * @auther GQY
 * @Date 2015/12/4.
 * 车辆简单信息 用于查询车辆详细使用 输入
 */
public class CarInfoSimpleBsEnter extends BsCvo {
    private static final long serialVersionUID = 6953930834845144513L;
    private String carNo;//车牌号
    private String carOwner;//车主姓名

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }
}
