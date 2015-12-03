package cn.com.qqbx.wx.bs.old;
//package cn.com.qqbx.wx.bs;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import cn.com.qqbx.wx.appbean.WechatPara;
//import cn.com.qqbx.wx.utils.WxXmlHelper;
//import cn.com.qqbx.wx.xmlbeans.Article;
//import cn.com.qqbx.wx.xmlbeans.ResBaseMessage;
//import cn.remex.core.Cvo;
//
//public class WxDiscountBs {
//	private Cvo cvo;
//	public WxDiscountBs(final Cvo cvo) {
//		this.cvo = cvo;
//	}
//public String controller(){
//	long createTime = new Date().getTime()/1000;
//	WechatPara wechatPara = (WechatPara)cvo.getBean();
//	ResBaseMessage rsbm = new ResBaseMessage();
//	rsbm.setToUserName(wechatPara.getFromUserName());
//	rsbm.setFromUserName(wechatPara.getToUserName());
//	rsbm.setCreateTime(createTime);
//	List<Article> articleList = new ArrayList<Article>();
//	Article article = new Article();
//	rsbm.setMsgType("news");
//	article.setTitle("民生银行");
//	article.setDescription("车险投保车险投保车险投保");
//	article.setPicUrl("http://www.qqbx.com.cn/wx/wx/images/minsheng.jpg");
////	article.setUrl("http://www.cmbc.com.cn");
//	articleList.add(article);
//	
//	Article article1 = new Article();
//	rsbm.setMsgType("news");
//	article1.setTitle("中国人寿保险股份有限公司");
//	article1.setDescription("车险投保车险投保车险投保");
//	article1.setPicUrl("http://www.qqbx.com.cn/wx/wx/images/guoshou.jpg");
//	article1.setUrl("http://www.chinalife.com.cn");
//	articleList.add(article1);
//	Article article2 = new Article();
//	rsbm.setMsgType("news");
//	article2.setTitle("英大泰和保险股份有限公司");
//	article2.setDescription("车险投保车险投保车险投保");
//	article2.setPicUrl("http://www.qqbx.com.cn/wx/wx/images/yingda.jpg");
//	article2.setUrl("http://211.160.72.161");
//	articleList.add(article2);
//	
//	Article article3 = new Article();
//	rsbm.setMsgType("news");
//	article3.setTitle("太平洋保险股份有限公司");
//	article3.setDescription("车险投保车险投保车险投保");
//	article3.setPicUrl("http://www.qqbx.com.cn/wx/wx/images/taibao.jpg");
//	article3.setUrl("http://www.ecpic.com.cn");
//	articleList.add(article3);
//	
//	Article article4 = new Article();
//	rsbm.setMsgType("news");
//	article4.setTitle("中海联合保险经纪公司");
//	article4.setDescription("中海联合保险经纪公司");
//	article4.setPicUrl("http://www.qqbx.com.cn/wx/wx/images/zh.jpg");
//	article4.setUrl("http://www.qqbx.com.cn/wx/wx/page/Test.jsp?a=123");
//	articleList.add(article4);
//	
//	rsbm.setArticleCount(articleList.size());
//	rsbm.setArticles(articleList);
//	
//	String responseBody = WxXmlHelper.marshall(rsbm, true);
//	System.out.println("响应报文"+responseBody);
//	return responseBody;
//}
//}
