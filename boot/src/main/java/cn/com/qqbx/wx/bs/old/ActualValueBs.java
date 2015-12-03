package cn.com.qqbx.wx.bs.old;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ReqActualValue;
import cn.com.qqbx.aitp.aiws.xmlbeans.actualvalue.ResActualValue;
import cn.com.qqbx.wx.appbean.ResultBean;
import cn.com.qqbx.wx.appbean.WxVehicle;
import cn.com.qqbx.wx.utils.WxUtil;
import cn.remex.bs.Bs;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;

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
	