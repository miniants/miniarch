package cn.com.qqbx.wx.bs.old;

import java.util.ArrayList;
import java.util.List;

import cn.com.qqbx.aitp.AitpUtils;
import cn.com.qqbx.aitp.aiws.xmlbeans.Request;
import cn.com.qqbx.aitp.aiws.xmlbeans.RequestBody;
import cn.com.qqbx.aitp.aiws.xmlbeans.quoteprice.ResQuotePriceInfo;
import cn.com.qqbx.aitp.aiws.xmlbeans.vehicle.ReqVehicleInfo;
import cn.com.qqbx.cpsp.chargews.xmlbeans.businessextend.ReqBusinessExtend;
import cn.com.qqbx.cpsp.chargews.xmlbeans.unifyplaceorder.ReqUnifyOrderProduct;
import cn.com.qqbx.cpsp.chargews.xmlbeans.unifyplaceorder.ReqUnifyPlaceOrder;
import cn.com.qqbx.cpsp.chargews.xmlbeans.unifyplaceorder.ResUnifyPlaceOrder;
import cn.com.qqbx.wx.appbean.ResultBean;
import cn.com.qqbx.wx.appbean.WxOrder;
import cn.com.qqbx.wx.appbean.WxVehicle;
import cn.com.qqbx.wx.text.HttpHelper;
import cn.com.qqbx.wx.text.XmlHelper;
import cn.com.qqbx.wx.utils.WxUtil;
import cn.com.qqbx.wx.utils.WxXmlHelper;
import cn.remex.bs.Bs;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.Extend;
import cn.remex.bs.Head;
import cn.remex.reflect.ReflectUtil;
import cn.remex.util.FileHelper;
import cn.remex.web.WebXmlHelper;

public class WxQueryQuotationBs implements Bs {
	
	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		
		
		
		
		
		WxOrder wxOrder = bsCvo.getBody(WxOrder.class);
		String userId = wxOrder.getOpenId();
		
		
		ReqUnifyPlaceOrder reqUnifyPlaceOrder = new ReqUnifyPlaceOrder();
		String contactName;             
		String email;              //邮箱
		String postAddr;           //寄送地址
		String postCode;           //寄送邮编
		String mobile;        //指定手机号
		String tel;           //电话
		contactName=wxOrder.getContactName();
		email = wxOrder.getEmail();
		postAddr = wxOrder.getPostAddr();
		postCode = wxOrder.getPostCode();
		mobile = wxOrder.getMobile();
		tel = wxOrder.getTel();
		
		reqUnifyPlaceOrder.setRcvName(contactName);
		reqUnifyPlaceOrder.setRcvMobile(mobile);
		reqUnifyPlaceOrder.setRcvEmail(email);
		reqUnifyPlaceOrder.setRcvPostAddress(postAddr);
		reqUnifyPlaceOrder.setRcvPostCode(postCode);
		reqUnifyPlaceOrder.setRcvTel(tel);
		reqUnifyPlaceOrder.setSuppllierCode("tb");
		reqUnifyPlaceOrder.setSuppllierName("太保");
		reqUnifyPlaceOrder.setTradeType("JSAPI");
		reqUnifyPlaceOrder.setBizNo(wxOrder.getBizOrderId());
		reqUnifyPlaceOrder.setBizDesc("车险订单");
		reqUnifyPlaceOrder.setChargeComCode("WX");
		
		
		
		ReflectUtil.copyProperties(reqUnifyPlaceOrder, wxOrder);
		reqUnifyPlaceOrder.setUserId(userId);
		
		List<ReqUnifyOrderProduct> reqUnifyOrderProducts = new ArrayList<ReqUnifyOrderProduct>();
		ReqUnifyOrderProduct reqUnifyOrderProduct = new ReqUnifyOrderProduct();
		ReflectUtil.copyProperties(reqUnifyOrderProduct, wxOrder);
		reqUnifyOrderProduct.setPrdtNo("1000");
		reqUnifyOrderProduct.setPrdtQuantity("1");
		reqUnifyOrderProducts.add(reqUnifyOrderProduct);
		reqUnifyPlaceOrder.setUnifyOrderProducts(reqUnifyOrderProducts);
		
		//cn.com.qqbx.cpsp.xmlbean.Request request = new cn.com.qqbx.cpsp.xmlbean.Request();
		//Head head = new Head();
		//head.setBs("CpspUnifyPlaceOrderBs");
		//request.setHead(head);
		//request.setBody(reqUnifyPlaceOrder);
		ReqBusinessExtend extend = new ReqBusinessExtend(true, "");
		extend.setTransChannel("MBWX");
		//request.setExtend(extend);
		//String req = AitpUtils.marshall(cn.com.qqbx.cpsp.xmlbean.Request.class,RequestBody.class,ReqUnifyPlaceOrder.class,request);
//		String url = "http://192.168.80.103:8080/Remex2/servlet/XmlService?rt=textStream&bs=AitpQuotePriceBs";
		//String url = "http://192.168.80.113:8080/cpsp/XmlService";
		//String responseXml = HttpHelper.sendXml(url,req);
		ResUnifyPlaceOrder rs = (ResUnifyPlaceOrder) WxUtil.invokeService("CpspUnifyPlaceOrderBs", "execute", reqUnifyPlaceOrder,extend, "ZHDX", "TEST");

		
		
		bsRvo.setBody(rs);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
		return bsRvo;
	}
	
	
}
	