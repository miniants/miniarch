package klb.appservice.service;

import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import klb.appservice.beans.CarInfoSimpleBsEnter;
import klb.appservice.beans.CarInfoSimpleBsOuter;
import org.springframework.stereotype.Service;

/**
 * @auther GQY
 * @Date 2015/12/4.
 */
@BusinessService
@Service
public class CarInfoSimple {
    @BusinessService
    public BsRvo execute(CarInfoSimpleBsEnter bsEnter) {
        CarInfoSimpleBsOuter carInfoSimpleBsOuter = new CarInfoSimpleBsOuter();
        if ("粤A7EV82".equals(bsEnter.getCarNo())){
            carInfoSimpleBsOuter.setCarNo("粤A7EV82");
            carInfoSimpleBsOuter.setOwner("张三");
            carInfoSimpleBsOuter.setOwnerId("142401195505083968");
            carInfoSimpleBsOuter.setBrandModel("大众FV7162FAAGG轿车");
            carInfoSimpleBsOuter.setVin("LFV2A2156E3043618");
            carInfoSimpleBsOuter.setEngineNo("V19558");
            carInfoSimpleBsOuter.setSignUpDate("2014-07-14");
            carInfoSimpleBsOuter.setTransfer(false);
            BsRvo bb=  new BsRvo(true,"success","0");
            bb.setBody(carInfoSimpleBsOuter);
            return bb;
        }
        return new BsRvo(true,"不存在","2",carInfoSimpleBsOuter);
    }
}
