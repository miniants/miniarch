package cn.com.qqbx.wx.bs.old;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqCustomerInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqPolicyEndDate;
import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ReqVehicleInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.policyenddate.ResPolicyEndDate;
import cn.com.qqbx.wx.appbean.ResultBean;
import cn.com.qqbx.wx.appbean.WxVehicle;
import cn.com.qqbx.wx.utils.WxUtil;
import cn.remex.bs.Bs;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.reflect.ReflectUtil;
import cn.remex.util.DateHelper;
import cn.remex.util.Judgment;

public class LastEndDateBs implements Bs {
	
	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		WxVehicle wxVehicle = bsCvo.getBody(WxVehicle.class);
		ReqPolicyEndDate reqPolicyEndDate = new ReqPolicyEndDate();
		ReqVehicleInfo reqVehicleInfo = new ReqVehicleInfo();
		ReqCustomerInfo reqCustomerInfo = new ReqCustomerInfo();
		List <ReqCustomerInfo> reqCustomerInfos = new ArrayList<ReqCustomerInfo>();
		
		ReflectUtil.copyProperties(reqVehicleInfo,wxVehicle);
		reqVehicleInfo.setTransferFlag("0");
		reqVehicleInfo.setRunMile("20000");
		
		reqVehicleInfo.setRunArea("02");
		reqVehicleInfo.setSpecialModelFlag("0");
		reqPolicyEndDate.setCityCode("110000");
		reqPolicyEndDate.setTaxStatus("0");
		reqCustomerInfo.setPersonType("2");
		reqCustomerInfo.setPersonClass("0");
		reqCustomerInfo.setCustomerType("1");
		reqCustomerInfo.setVehicleRelation("1");
		reqCustomerInfo.setName(wxVehicle.getOwner());
		reqCustomerInfo.setSex("1");
		reqCustomerInfo.setCertType("01");
		reqCustomerInfo.setCertNo(wxVehicle.getOwnerCertNo());
		reqCustomerInfos.add(reqCustomerInfo);
		ReqCustomerInfo reqCustomerInfo1 = new ReqCustomerInfo();
		
		reqCustomerInfo1.setPersonType("1");
		reqCustomerInfo1.setPersonClass("0");
		reqCustomerInfo1.setCustomerType("1");
		reqCustomerInfo1.setVehicleRelation("1");
		reqCustomerInfo1.setName(wxVehicle.getOwner());
		reqCustomerInfo1.setSex("1");
		reqCustomerInfo1.setCertType("01");
		reqCustomerInfo1.setCertNo(wxVehicle.getOwnerCertNo());
		reqCustomerInfos.add(reqCustomerInfo1);
		
		ReqCustomerInfo reqCustomerInfo2 = new ReqCustomerInfo();
		reqCustomerInfo2.setPersonType("0");
		reqCustomerInfo2.setPersonClass("0");
		reqCustomerInfo2.setCustomerType("1");
		reqCustomerInfo2.setVehicleRelation("1");
		reqCustomerInfo2.setName(wxVehicle.getOwner());
		reqCustomerInfo2.setSex("1");
		reqCustomerInfo2.setCertType("01");
		reqCustomerInfo2.setCertNo(wxVehicle.getOwnerCertNo());
		reqCustomerInfos.add(reqCustomerInfo2);
		reqPolicyEndDate.setVehicleInfo(reqVehicleInfo);
		reqPolicyEndDate.setAtinCustomers(reqCustomerInfos);
		
		ResPolicyEndDate resPolicyEndDate =(ResPolicyEndDate) WxUtil.invokeService("AitpQueryPolicyEndDateBs", "queryLastContEndDate", reqPolicyEndDate,null, "ZHDX", "TEST");
		String tciLastEndDate = resPolicyEndDate.getTciLastEndDate();
		String vciLastEndDate = resPolicyEndDate.getVciLastEndDate();
		String tciStartDate = DateHelper.formatDate(DateHelper.getDate(Judgment.nullOrBlank(tciLastEndDate)?new Date():DateHelper.parseDate(tciLastEndDate), 1));
		String vciStartDate = DateHelper.formatDate(DateHelper.getDate(Judgment.nullOrBlank(vciLastEndDate)?new Date():DateHelper.parseDate(vciLastEndDate), 1));
		wxVehicle.setTciLastEndDate(tciLastEndDate);
		wxVehicle.setVciLastEndDate(vciLastEndDate);
		wxVehicle.setTciStartDate(tciStartDate);
		wxVehicle.setVciStartDate(vciStartDate);
		bsRvo.setBody(wxVehicle);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
	
		return bsRvo;

	}
}
	