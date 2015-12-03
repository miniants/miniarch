package anbox.aibc.bl;

import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.appBeans.mmbCenter.MmbCenterRvo;
import anbox.aibc.model.member.Member;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.reflect.ReflectUtil;

/**会员主页
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class MemberCenterBs  implements AiwbConsts{

	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=MmbCenterRvo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		//获取jsapp请求数据
		MmbCenterCvo mmbCenterCvo = bsCvo.getBody();
		final MemberExtend extend = bsCvo.getExtend();
		//验证请求类型
		if(!ReqInfoType.MC.toString().equals(mmbCenterCvo.getReqInfoType())){
			bsRvo.setExtend(new MemberExtend(false, "请求查看的数据类型错误!"));
			return bsRvo;
		}
		MmbCenterRvo mmbCenterRvo = new MmbCenterRvo();
		// 此部分代码存在严重的内存及数据库资源耗尽漏洞 BUG1 TODO  LHY 
		//查询会员的数据库订单信息
//		DbRvo dbRvo = ContainerFactory.getSession().query(
//				new DbCvo<ProductOrder>(ProductOrder.class){
//					private static final long serialVersionUID = -8622419602237050312L;
//					@Override
//					public void initRules(ProductOrder t) {
//						setDoCount(true);
//						setDoPaging(true);
//						setRowCount(extend.getRowCount()<20?extend.getRowCount():20);
//						setPagination(extend.getPagination()<1?0:extend.getPagination());
//						addRule(t.getMember()+"", WhereRuleOper.eq, curMember.getId());
//						addRule(t.getOrderStatus()+"", WhereRuleOper.in, String.valueOf(OrderStatus.OSNP)+","+String.valueOf(OrderStatus.OSFP));
//						addRule(t.getCreateTime()+"", WhereRuleOper.in, "2015-01-27");
//					}
//				});
//		List<ProductOrder> productOrders = dbRvo.obtainBeans();
		
//		List<ToPayOrder> toPayOrders = new ArrayList<ToPayOrder>();
//		for(ProductOrder productOrder : productOrders){//TODO TODO 校验订单明细还是保单表
//			List<ProductOrderDetail> details = productOrder.getOrderDetails();
//			for(ProductOrderDetail productOrderDetail : details){
//				AtinPolicy atinPolicy = ContainerFactory.getSession().queryBeanById(AtinPolicy.class, productOrderDetail.getPolicyNo());//第一版只有车险
//				Date nowDate = DateHelper.parseDate(DateHelper.getNow(AiwbConsts.DateFormat_date), AiwbConsts.DateFormat_date);
//				long tciStartDate = DateHelper.parseDate(atinPolicy.getTciStartDate(), AiwbConsts.DateFormat_date).getTime();
//				long vciStartDate = DateHelper.parseDate(atinPolicy.getVciStartDate(), AiwbConsts.DateFormat_date).getTime();
//				if(AiwbConsts.PolicyStatus_toPay.contains(atinPolicy.getPolicyStatus()) && 
//						AiwbConsts.PayStatus_toPay.contains(atinPolicy.getPayStatus()) &&
//						(tciStartDate>nowDate.getTime() && vciStartDate>nowDate.getTime())){
//					ToPayOrder toPayOrder = new ToPayOrder();
//					toPayOrders.add(toPayOrder);
//					ReflectUtil.copyProperties(toPayOrder, atinPolicy);
//					toPayOrder.setOrderNo(productOrder.getId());//设置订单号
//					for(AtinPlcyCustomer apc : atinPolicy.getAtinCustomers()){
//						if(personType_app.equals(apc.getPersonType()))
//							toPayOrder.setApplicantName(apc.getName());
//					}
////					toPayOrder.setPackageType(packageType)TODO
//					toPayOrder.setInsureDate(atinPolicy.getCreateTime());//TODO
////					toPayOrder.setOrderStatus(orderStatus);//TODO
//				}
//			}
//		}
		//BUG 1 end
		
		ReflectUtil.copyProperties(mmbCenterRvo, curMember);
		mmbCenterRvo.setMobile(curMember.getMobile());//平台映射信息导致复制异常，须单独set
//		mmbCenterRvo.setCustomerCount(curMember.getCustomerCount());
//		mmbCenterRvo.setLevel(curMember.getLevel());
//		mmbCenterRvo.setNickname(curMember.getNickname());
//		mmbCenterRvo.setUsername(curMember.getUsername());
//		mmbCenterRvo.setCoinCount(curMember.getCoinCount());
//		mmbCenterRvo.setBeanCount(curMember.getBeanCount());
		List<Member> subLineCount = curMember.getSubLines();
		mmbCenterRvo.setSubLineCount(null!=subLineCount?subLineCount.size():0);
		
//		mmbCenterRvo.setToPayOrders(toPayOrders);
		bsRvo.setBody(mmbCenterRvo);
		extend.setStatus(true);
		extend.setMsg("查询完成!");
//		extend.setRecordCount(dbRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}
}
