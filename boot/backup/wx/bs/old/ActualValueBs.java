package zbh.wx.bs.old;

import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
import zbh.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
import zbh.wx.appbean.ResultBean;
import zbh.wx.appbean.WxVehicle;
import zbh.wx.utils.WxUtil;
import zbh.remex.bs.Bs;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;

public class ActualValueBs implements Bs {
	
	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		
		
		
		
		WxVehicle wxVehicle = bsCvo.getBody(WxVehicle.class);

		ReqActualValue reqActualValue = new ReqActualValue();
		reqActualValue.setCityCode("110000");
		reqActualValue.setInsuCom(WxConst.insuCom);
		reqActualValue.setPurchasePrice(String.valueOf(wxVehicle.getPurchasePrice()));
		reqActualValue.setEnrollDate(wxVehicle.getEnrollDate());
		reqActualValue.setVciStartDate("2014-12-08");
		reqActualValue.setUseType("8A");
		reqActualValue.setVehicleKind("A0");
		reqActualValue.setSeatCount(String.valueOf(wxVehicle.getSeatCount()));
		reqActualValue.setVehicleTonnage("0.0");
	
		ResActualValue resActualValue =(ResActualValue) WxUtil.invokeService("AitpActualValueBs", "queryCarRealPrice", reqActualValue,null,"ZHDX", "TEST");
		wxVehicle.setActualValue(Double.parseDouble(resActualValue.getActualValue()));
		bsRvo.setBody(wxVehicle);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
		return bsRvo;

	}

}
	