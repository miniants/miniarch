package zbh.aias.appservice.service;

import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;
import zbh.aias.appservice.beans.VehicleModelBsEnter;
import zbh.aias.appservice.beans.VehicleModelBsOuter;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther GQY
 * @Date 2015/12/5.
 * 车型查询
 */
@BusinessService
@Service
public class VehicleModel {
    @BusinessService
    public BsRvo execute(VehicleModelBsEnter bsEnter) {
        List<VehicleModelBsOuter> list = new ArrayList<VehicleModelBsOuter>();
        if(null!=bsEnter.getBrandName()&&!"bb".equals(bsEnter.getBrandName())){
            for (int i = 0; i < 4; i++) {
                VehicleModelBsOuter vechModelBsOuter = new VehicleModelBsOuter();
                vechModelBsOuter.setrBCode("a"+i);
                vechModelBsOuter.setModelName("上海通用别克凯越"+i+100);
                list.add(vechModelBsOuter);
            }
        }
        return new BsRvo(true,list);
    }
}
