package cn.com.qqbx.wx.fsmaction;

import java.util.ArrayList;
import java.util.List;

import cn.com.qqbx.wx.xmlbeans.Article;
import cn.com.qqbx.wx.xmlbeans.CustomMessage;
import cn.com.qqbx.wx.xmlbeans.ResBaseMessage;
import cn.remex.fsm.FsmAction;
import cn.remex.fsm.FsmResult;
import cn.remex.sam.SamConstant.SamMessageType;

public class ClickAncarAction extends FsmAction {

	private static final long serialVersionUID = 619476554085709434L;
	@Override
	public FsmResult execute() {
		ResBaseMessage rsbm = new ResBaseMessage();
		FsmResult fsmResult;
		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
		CustomMessage cm = new CustomMessage();
		article.setTitle("");
//		article.setPicurl("http://www.qqbx.com.cn/wx/wx/images/test1.jpg");
		articleList.add(article);
		
		Article article1 = new Article();
		article1.setTitle("省流量投保请输入车险");
		article1.setDescription("省流量投保请输入车险");
//		article1.setPicurl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx16f616bf5fa97471&redirect_uri=http://www.qqbx.com.cn/wx/wx/page/method.html&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirec");
		article1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx16f616bf5fa97471&redirect_uri=http://www.qqbx.com.cn/wx/wx/page/method.html&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
		articleList.add(article1);
		
		Article article2 = new Article();
		article2.setTitle("欢迎点击进入安卡车险在线比价");
		article2.setDescription("页面投保请点击");
//		article2.setPicurl("http://www.qqbx.com.cn/wx/wechat/image/yingda1.jpg");
		article2.setUrl("http://www.qqbx.com.cn/wx/wx/page/carinfo.html?"+fsmContext.getUserId());
		articleList.add(article2);
		rsbm.setArticleCount(articleList.size());
		rsbm.setArticles(articleList);
		rsbm.setMsgType("news");
		fsmResult = new FsmResult(SamMessageType.XmlObj,rsbm,fsmContext);
		return fsmResult;
	}
	

}
