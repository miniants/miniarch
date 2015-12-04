package zbh.wx.bs.old;
//package zbh.com.qqbx.wx.bs;
//
//import java.util.List;
//
//import zbh.com.qqbx.wx.appbean.AllInfoOfPrice;
//import ResultBean;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnCoverage;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQuotation;
//import zbh.com.qqbx.wx.model.wxvehicle.Vehicle;
//import zbh.remex.bs.Bs;
//import zbh.remex.bs.BsCvo;
//import zbh.remex.bs.BsRvo;
//import ContainerFactory;
//import DbCvo;
//import DbRvo;
//import RsqlConstants.WhereRuleOper;
//
//public class WxMyQuotePriceBs implements Bs {
//	
//	@Override
//	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
//		AtinQuotation atinQuotation = bsCvo.getBody(AtinQuotation.class);
//		final String quoteNo = atinQuotation.getQuoteNo();
//		DbRvo res = ContainerFactory.getSession().query(new DbCvo<AtinQuotation>(AtinQuotation.class){
//			private static final long serialVersionUID = -2291709702597738740L;
//			@Override
//			public void initRules(AtinQuotation v) {
//				addRule(v.getQuoteNo(), WhereRuleOper.zbh,quoteNo);
//			}
//		});
//		List<AtinQuotation> atinQuotations = res.obtainBeans();
//		List<AtinQttnCoverage> atinCoverages = atinQuotations.get(0).getAtinCoverages();
//		AllInfoOfPrice allInfoOfPrice = new AllInfoOfPrice();
//		for(int i=0;i<atinCoverages.size();i++){
//			if("M".equals(atinCoverages.get(i).getCoverageCode())){
//				allInfoOfPrice.setIsNoDutyInfoFlag("1");
//			}else{
//				allInfoOfPrice.setIsNoDutyInfoFlag("0");
//			}
//		}
//		allInfoOfPrice.setAtinCoverages(atinQuotations.get(0).getAtinCoverages());
//		System.out.println("");
//		bsRvo.setBody(allInfoOfPrice);
//		bsRvo.setExtend(new ResultBean(true, "操作成功"));
//		return bsRvo;
//	}
//}
//	
//	