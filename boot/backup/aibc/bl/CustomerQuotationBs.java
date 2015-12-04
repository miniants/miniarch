package zbh.aibc.bl;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.appBeans.mmbCenter.Quotation;
import zbh.aibc.appBeans.mmbCenter.QuotationRvo;
import zbh.aias.model.quotation.AtinQttnCustomer;
import anbox.aibc.model.quotation.AtinQuotation;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.reflect.ReflectUtil.SPFeature;
import zbh.remex.util.Judgment;

/**我的询价单
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class CustomerQuotationBs  implements AiwbConsts{
	
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=QuotationRvo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		//获取jsapp请求数据
		MmbCenterCvo mmbCenterCvo = bsCvo.getBody();
		final MemberExtend extend = bsCvo.getExtend();
		//验证请求类型
		if(!ReqInfoType.CQ.toString().equals(mmbCenterCvo.getReqInfoType())){
			bsRvo.setExtend(new MemberExtend(false, "查看询价信息的请求数据类型错误!"));
			return bsRvo;
		}
		
//		final MemberExtend memberExtend = bsCvo.getExtend();//获取扩展信息
		List<Quotation> quotations = new ArrayList<Quotation>();//rvo的list询价单
		//查询会员的数据库询价单信息
		DbRvo dbRvo = ContainerFactory.getSession().query(
				new DbCvo<AtinQuotation>(AtinQuotation.class){
					private static final long serialVersionUID = -7217536947099474510L;
					@Override
					public void initRules(AtinQuotation t) {
						setDoCount(true);
						setDoPaging(true);
						setRowCount(extend.getRowCount()<20?extend.getRowCount():20);
						setPagination(extend.getPagination()<1?0:extend.getPagination());
						setDataType("bd");
						if(null !=extend.getFilters()){
							setSqlBeanWhere(extend.getFilters());
						}
						addRule(""+t.getOpenId(), WhereRuleOper.eq, uid);
						addRule(t.getDisplayStatus(), WhereRuleOper.eq, DisplayStatus_Y);
					}
				});
		List<AtinQuotation> atinQuotations = dbRvo.obtainBeans();
		for(AtinQuotation atinQuotation : atinQuotations){
			Quotation quotation = new Quotation();
			quotation.setQuotePriceDate(atinQuotation.getCreateTime());//TODO
			quotation.setQuotationStatus(atinQuotation.getQuotationStatus());
			ReflectUtil.copyProperties(quotation, atinQuotation,SPFeature.DeeplyCopy);
			quotation.setQuoteNo(atinQuotation.getId());
			List<AtinQttnCustomer> atinCustomers = atinQuotation.getAtinCustomers();
			for(AtinQttnCustomer atinQttnCustomer : atinCustomers){
				if(!personType_ins.equals(atinQttnCustomer.getPersonType())){
					continue;
				}
				quotation.setInsuredName(atinQttnCustomer.getName());
				quotation.setInsuredCertNo(atinQttnCustomer.getCertNo());
			}
			quotations.add(quotation);
		}
		
		QuotationRvo quotationRvo = new QuotationRvo();
		quotationRvo.setQuotations(quotations);
		bsRvo.setBody(quotationRvo);
//		MemberExtend memberExtend = new MemberExtend(false, null);
		extend.setStatus(true);
		extend.setMsg("询价信息查询完毕");
		extend.setPageCount(AiwbUtils.obtainPageCount(dbRvo.getRecordCount(),extend.getRowCount()));
		extend.setRecordCount(dbRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}

}
