package cn.com.qqbx.wx.text;

import java.util.ArrayList;
import java.util.List;

import cn.com.qqbx.wx.xmlbeans.Article;
import cn.com.qqbx.wx.xmlbeans.CustomMessage;
import cn.com.qqbx.wx.xmlbeans.News;
import cn.com.qqbx.wx.xmlbeans.ResBaseMessage;
import cn.remex.fsm.FsmResult;
import cn.remex.sam.SamConstant.SamMessageType;
import cn.remex.util.JsonHelper;

public class Kefu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
		CustomMessage cm = new CustomMessage();
		article.setTitle("保险公司报价信息");
		article.setPicurl("http://www.qqbx.com.cn/wx/wechat/image/test.jpg");
		articleList.add(article);
		String msg;
			Article article1 = new Article();
				msg = "国寿报价:总保费"+"1231"+","+"交强险保费:"+"1233";
				article1.setTitle(msg);
				article1.setDescription("车险投保车险投保车险投保");
				article1.setPicurl("http://www.qqbx.com.cn/wx/wechat/image/guoshou.jpg");
				articleList.add(article1);
				cm.setTouser("a");
				cm.setMsgtype("news");
				News news = new News();
				news.setArticles(articleList);
				cm.setNews(news);
		System.out.println(JsonHelper.toJsonString(cm));
				
	}

}
