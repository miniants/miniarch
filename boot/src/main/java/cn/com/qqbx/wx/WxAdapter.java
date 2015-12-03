package cn.com.qqbx.wx;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.qqbx.wx.appbean.WechatPara;
import cn.com.qqbx.wx.appbean.WxCheck;
import cn.com.qqbx.wx.utils.WxXmlHelper;
import cn.com.qqbx.wx.xmlbeans.ReqBaseMessage;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.Head;
import cn.remex.util.Judgment;
import cn.remex.util.MapHelper;
import cn.remex.web.ObtainFromRequest;
import cn.remex.web.xml.AdapterAppBean;
import cn.remex.web.xml.XmlCvo;
import cn.remex.web.xml.XmlRvo;
import cn.remex.web.xml.XmlServiceAdapter;

public class WxAdapter implements XmlServiceAdapter{
	@Override
	public AdapterAppBean inBound(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		XmlCvo bsCvo = new XmlCvo(){

			@Override
			public <T> T getBody(Class<T> bodyClass) {
				// TODO Auto-generated method stub
				return (T) this.body;
			}
			
		};
		XmlRvo bsRvo = new XmlRvo();
		
		String pack = ObtainFromRequest.getDataFromRequest(request);
		Head head = new Head();
		bsCvo.setParameters(request.getParameterMap());
		if(Judgment.nullOrBlank(pack)){
			head.setBs("WxServiceBs");
			head.setBsCmd("executeCheck");
			head.setRt("textStream");
			bsCvo.setBody(MapHelper.toObject(bsCvo.getParameters(), WxCheck.class));
			
			
		}else{
			ReqBaseMessage wx = WxXmlHelper.unmarshall(ReqBaseMessage.class, pack);
			bsCvo.setBody(WxXmlHelper.unmarshall(ReqBaseMessage.class, pack));
			head.setBs("WxServiceBs");
			head.setBsCmd("execute");
			head.setRt("xmlBody");
		}
		bsCvo.setHead(head);
		bsRvo.setHead(head);
		
		
		
		AdapterAppBean adapter = new AdapterAppBean(bsCvo, bsRvo);
		return adapter;
	}

	@Override
	public boolean outBound(HttpServletRequest request, HttpServletResponse response,BsCvo bsCvo,BsRvo bsRvo) {
		
		
		String s = WxXmlHelper.marshall(bsRvo.getBody(), true);
		if("xmlBody".equals(bsRvo.getHead().getRt())){
			
			try {
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
