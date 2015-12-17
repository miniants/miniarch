package zbh.aias.appservice.service;

import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;
import zbh.aias.appservice.beans.QuotationBsOuter;
import zbh.aias.appservice.beans.VehicleModelBsEnter;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther GQY
 * @Date 2015/12/7.
 */
@BusinessService
@Service
public class Quotation {
    @BusinessService
    public BsRvo execute() {
        List<QuotationBsOuter> list = new ArrayList<>();
        QuotationBsOuter quotationBsOuter = new QuotationBsOuter();
        quotationBsOuter.setInsuCom("I00001");
        quotationBsOuter.setInsuName("中国人保车险");
        quotationBsOuter.setTciPremium("600.00");
        quotationBsOuter.setSumTravelTax("400.00");
        quotationBsOuter.setSumPremium("5203.95");
        quotationBsOuter.setaPremium("1377.11");
        quotationBsOuter.setaBMPremium("206.57");
        quotationBsOuter.setbPremium("1013.31");
        quotationBsOuter.setbAmount("50万");
        quotationBsOuter.setG1Premium("456.96");
        quotationBsOuter.setG1MPremium("91.39");
        quotationBsOuter.setD3Premium("28.23");
        quotationBsOuter.setD3Amount("1万");
        quotationBsOuter.setD3MPremium("4.23");
        quotationBsOuter.setD4Premium("71.60");
        quotationBsOuter.setD4Amount("1万");
        quotationBsOuter.setD4MPremium("10.74");
        quotationBsOuter.setfPremium("161.30");
        quotationBsOuter.setfModel("国产");
        quotationBsOuter.setX1Premium("68.86");
        quotationBsOuter.setlPremium("420.39");
        quotationBsOuter.setlAmount("1000");
        quotationBsOuter.setzPremium("141.26");
        list.add(quotationBsOuter);
        QuotationBsOuter quotationBsOuter1 = new QuotationBsOuter();
        quotationBsOuter1.setInsuCom("I00002");
        quotationBsOuter1.setInsuName("中国平安车险");
        quotationBsOuter1.setTciPremium("600.00");
        quotationBsOuter1.setSumTravelTax("400.00");
        quotationBsOuter1.setSumPremium("6403.95");
        quotationBsOuter1.setaPremium("1377.11");
        quotationBsOuter1.setaBMPremium("206.57");
        quotationBsOuter1.setbPremium("1013.31");
        quotationBsOuter1.setbAmount("50万");
        quotationBsOuter1.setG1Premium("456.96");
        quotationBsOuter1.setG1MPremium("91.39");
        quotationBsOuter1.setD3Premium("28.23");
        quotationBsOuter1.setD3Amount("1万");
        quotationBsOuter1.setD3MPremium("4.23");
        quotationBsOuter1.setD4Premium("71.60");
        quotationBsOuter1.setD4Amount("1万");
        quotationBsOuter1.setD4MPremium("10.74");
        quotationBsOuter1.setfPremium("161.30");
        quotationBsOuter1.setfModel("国产");
        quotationBsOuter1.setX1Premium("68.86");
        quotationBsOuter1.setlPremium("420.39");
        quotationBsOuter1.setlAmount("1000");
        quotationBsOuter1.setzPremium("141.26");
        list.add(quotationBsOuter1);
        QuotationBsOuter quotationBsOuter2 = new QuotationBsOuter();
        quotationBsOuter2.setInsuCom("I00003");
        quotationBsOuter2.setInsuName("太平洋车险");
        quotationBsOuter2.setTciPremium("600.00");
        quotationBsOuter2.setSumTravelTax("400.00");
        quotationBsOuter2.setSumPremium("4403.95");
        quotationBsOuter2.setaPremium("1377.11");
        quotationBsOuter2.setaBMPremium("206.57");
        quotationBsOuter2.setbPremium("1013.31");
        quotationBsOuter2.setbAmount("50万");
        quotationBsOuter2.setG1Premium("456.96");
        quotationBsOuter2.setG1MPremium("91.39");
        quotationBsOuter2.setD3Premium("28.23");
        quotationBsOuter2.setD3Amount("1万");
        quotationBsOuter2.setD3MPremium("4.23");
        quotationBsOuter2.setD4Premium("71.60");
        quotationBsOuter2.setD4Amount("1万");
        quotationBsOuter2.setD4MPremium("10.74");
        quotationBsOuter2.setfPremium("161.30");
        quotationBsOuter2.setfModel("国产");
        quotationBsOuter2.setX1Premium("68.86");
        quotationBsOuter2.setlPremium("420.39");
        quotationBsOuter2.setlAmount("1000");
        quotationBsOuter2.setzPremium("141.26");
        list.add(quotationBsOuter2);
        return new BsRvo(true, list);
    }
}
