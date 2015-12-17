package zbh.aias.appservice.service;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;
import zbh.aias.appservice.beans.CarInfoSimpleBsEnter;
import zbh.aias.appservice.beans.CarInfoSimpleBsOuter;

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
            if ("粤A7EV82".equals(bsEnter.getCarNo())) {
                carInfoSimpleBsOuter.setLicenseNo("粤A7EV82");
                carInfoSimpleBsOuter.setApplicantName("张三");
                carInfoSimpleBsOuter.setApplicantId("142401195505083968");
                carInfoSimpleBsOuter.setBrandName("大众FV7162FAAGG轿车");
                carInfoSimpleBsOuter.setFrameNo("LFV2A2156E3043618");
                carInfoSimpleBsOuter.setEngineNo("V19558");
                carInfoSimpleBsOuter.setEnrollDate("2014-07-14");
                carInfoSimpleBsOuter.setTransferFlag("1");
                carInfoSimpleBsOuter.setSameOwner("0");
                carInfoSimpleBsOuter.setrBCode("a2");
                BsRvo bb = new BsRvo(true, "success", "0");
                bb.setBody(carInfoSimpleBsOuter);
                return bb;
            }
        return new BsRvo(true,"不存在","2",carInfoSimpleBsOuter);
    }
}
