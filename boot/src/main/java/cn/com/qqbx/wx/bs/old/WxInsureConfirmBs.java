package cn.com.qqbx.wx.bs.old;
//package cn.com.qqbx.wx.bs;
//
//import java.util.List;
//
//import cn.com.qqbx.aitp.aiws.xmlbeans.insuconfirm.ReqInsureConfirmInfo;
//import cn.com.qqbx.aitp.aiws.xmlbeans.insuconfirm.ResInsureConfirmInfo;
//import cn.com.qqbx.wx.appbean.ResultBean;
//import cn.com.qqbx.wx.appbean.WxInsure;
//import cn.com.qqbx.wx.appbean.WxOrder;
//import cn.com.qqbx.wx.model.WxPost;
//import cn.com.qqbx.wx.model.wxorder.Order;
//import cn.com.qqbx.wx.model.wxquotation.AtinQttnCoverage;
//import cn.com.qqbx.wx.model.wxquotation.AtinQuotation;
//import cn.com.qqbx.wx.utils.WxUtil;
//import cn.remex.bs.Bs;
//import cn.remex.bs.BsCvo;
//import cn.remex.bs.BsRvo;
//import cn.remex.bs.Extend;
//import cn.remex.db.ContainerFactory;
//import cn.remex.db.DbCvo;
//import cn.remex.db.DbRvo;
//import cn.remex.db.rsql.RsqlConstants;
//import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
//import cn.remex.reflect.ReflectUtil;
//
//public class WxInsureConfirmBs implements Bs {
//
//	@Override
//	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
//		WxInsure wxInsure = bsCvo.getBody(WxInsure.class);
//		final String quoteNo = wxInsure.getQuoteNo();
//		final String openId = wxInsure.getOpenId();
//		// String openId = wxInsure.getOpenId();
//		ReqInsureConfirmInfo reqInsureConfirmInfo = new ReqInsureConfirmInfo();
//
//		reqInsureConfirmInfo.setCityCode("110000");
//		reqInsureConfirmInfo.setInsuCom(wxInsure.getInsuCom());
//		reqInsureConfirmInfo.setQuoteNo(quoteNo);
//		
//		try {
//			ResInsureConfirmInfo resInsureConfirmInfo = (ResInsureConfirmInfo) WxUtil.invokeService("AitpInsureConfirmBs", "beginInsure", reqInsureConfirmInfo,null, "ZHDX", "zag");
//			
//			DbRvo res = ContainerFactory.getSession().query(new DbCvo<AtinQuotation>(AtinQuotation.class) {
//				private static final long serialVersionUID = -2291709702597738740L;
//				
//				@Override
//				public void initRules(AtinQuotation v) {
//					// setSearch(true);
//					// 设置查询条件
//					setDataType(RsqlConstants.DT_base + RsqlConstants.DT_object);
//					
//					addRule(v.getQuoteNo(), WhereRuleOper.cn, quoteNo);
//				}
//			});
//			List<AtinQuotation> atinQuotations = res.obtainBeans();
//			double premium = atinQuotations.get(0).getPremium();
//			List<AtinQttnCoverage> atinQttnCoverages = atinQuotations.get(0).getAtinCoverages();
//			StringBuffer sb = new StringBuffer();
//			for (int i = 0; i < atinQttnCoverages.size(); i++) {
//				sb.append(atinQttnCoverages.get(i).getCoverageName());
//				sb.append(" ");
//			}
//			String prdtNama = sb.toString();
//			
//			Order order = new Order();
//			order.setBizOrderId(resInsureConfirmInfo.getInnerPlcyNo());
//			order.setPrdtAmount(premium+"");
//			if("I00004".equals(atinQuotations.get(0).getInsuCom())){
//				order.setPrdtType("富德车险");
//			}else if("I00002".equals(atinQuotations.get(0).getInsuCom())){
//				order.setPrdtType("英大车险");
//			}else if("I00003".equals(atinQuotations.get(0).getInsuCom())){
//				order.setPrdtType("国寿车险");
//			}else if("I00001".equals(atinQuotations.get(0).getInsuCom())){
//				order.setPrdtType("太保车险");
//			}
//			order.setPrdtName(prdtNama);
//			
//			DbRvo resp = ContainerFactory.getSession().query(new DbCvo<WxPost>(WxPost.class) {
//				private static final long serialVersionUID = -2291709702597738740L;
//				
//				@Override
//				public void initRules(WxPost v) {
//					addRule(v.getOpenId(), WhereRuleOper.cn, openId);
//				}
//			});
//			List<WxPost> wxPosts = resp.obtainBeans();
//			
//			WxOrder wxOrder = new WxOrder();
//			if (wxPosts.size() > 0)
//				ReflectUtil.copyProperties(wxOrder, wxPosts.get(0));
//			ReflectUtil.copyProperties(wxOrder, order);
//			order.setOpenId(openId);
//			ContainerFactory.getSession().store(order);
//			
//			bsRvo.setBody(wxOrder);
//			bsRvo.setExtend(new ResultBean(true, "操作成功"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			bsRvo.setExtend(new Extend(false, e.getMessage()));
//		}
//		
//		
//		return bsRvo;
//	}
//}
