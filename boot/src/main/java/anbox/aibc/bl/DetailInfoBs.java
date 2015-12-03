package anbox.aibc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.appBeans.mmbCenter.OrderInfo;
import anbox.aibc.appBeans.mmbCenter.OrderRvo;
import anbox.aibc.appBeans.mmbCenter.Quotation;
import anbox.aibc.appBeans.mmbCenter.QuotationRvo;
import anbox.aibc.model.quotation.AtinQuotation;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbRvo;
import cn.remex.reflect.ReflectUtil;
import cn.remex.reflect.ReflectUtil.SPFeature;
import cn.remex.util.Judgment;

/**询价、订单详细信息查询
 * @author zhangaiguo
 * @since 2015-02-04
 */
@BsAnnotation
public class DetailInfoBs implements AiwbConsts{
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=QuotationRvo.class,
			bsRvoExtendClass=MemberExtend.class)
	public BsRvo execQuotationDetail(BsCvo bsCvo, BsRvo bsRvo) {
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody(	);
		if(Judgment.nullOrBlank(mCvo.getId())){
			bsRvo.setExtend(new MemberExtend(false, "请提供查询id!"));
			return bsRvo;
		}
		
		AtinQuotation atinQuotation = ContainerFactory.getSession().queryBeanById(AtinQuotation.class,mCvo.getId());
		if(null == atinQuotation){
			bsRvo.setExtend(new MemberExtend(false, "未查询到详情!"));
			return bsRvo;
		}
		
		if(!uid.equals(atinQuotation.getOpenId())){
			bsRvo.setExtend(new MemberExtend(false, "权限错误!"));
			return bsRvo;
		}
		QuotationRvo quotationRvo = new QuotationRvo();
		List<Quotation> list = new ArrayList<Quotation>();
		Quotation q = new Quotation();
		ReflectUtil.copyProperties(q, atinQuotation, SPFeature.DeeplyCopy);
		q.setQuotePriceDate(atinQuotation.getCreateTime());
		list.add(q);
		quotationRvo.setQuotations(list);
		
		bsRvo.setBody(quotationRvo);
		bsRvo.setExtend(new MemberExtend(true, "查询完毕!"));
		return bsRvo;
	}
	
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=OrderRvo.class,
			bsRvoExtendClass=MemberExtend.class)
	public BsRvo execOrderDetail(BsCvo bsCvo, BsRvo bsRvo) {
		
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		MmbCenterCvo mCvo = bsCvo.getBody(	);
		if(Judgment.nullOrBlank(mCvo.getId())){
			bsRvo.setExtend(new MemberExtend(false, "请提供查询id!"));
			return bsRvo;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> sqlMap = (Map<String, String>)AiwbUtils.getBean("Order_SQL");
		String sql = sqlMap.get("SQL_OrderDetail");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", mCvo.getId());
		params.put("openId", uid);
		DbRvo dbRvo = ContainerFactory.getSession().executeQuery(sql, params);
		List<OrderInfo> os = dbRvo.obtainObjects(OrderInfo.class);
		if(null == os || 0== os.size()){
			bsRvo.setExtend(new MemberExtend(false, "未查询到详情!"));
			return bsRvo;
		}
		
		OrderRvo orderRvo = new OrderRvo();
		orderRvo.setOrders(os);
		bsRvo.setBody(orderRvo);
		bsRvo.setExtend(new MemberExtend(true, "保存地址的服务尚未开发完成！"));
		return bsRvo;
	}
}
