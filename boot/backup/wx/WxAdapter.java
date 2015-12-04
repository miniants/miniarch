package zbh.wx;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zbh.wx.appbean.WxCheck;
import zbh.wx.utils.WxXmlHelper;
import zbh.wx.xmlbeans.ReqBaseMessage;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.Head;
import zbh.remex.util.Judgment;
import zbh.remex.util.MapHelper;
import zbh.remex.web.ObtainFromRequest;
import zbh.remex.web.xml.AdapterAppBean;
import zbh.remex.web.xml.XmlCvo;
import zbh.remex.web.xml.XmlRvo;
import zbh.remex.web.xml.XmlServiceAdapter;

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
