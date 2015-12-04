package zbh.aibc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aibc.appBeans.mmbCenter.MmbCenterCvo;
import zbh.aibc.appBeans.mmbCenter.OrderInfo;
import zbh.aibc.appBeans.mmbCenter.OrderRvo;
import zbh.aias.model.member.Member;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.model.Modelable;
import zbh.remex.util.Judgment;

/**客户订单
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class CustomerOrderBs  implements AiwbConsts{
	public final static Map<String, String> orderRegMap = new HashMap<String, String>();
	static{
		orderRegMap.put("licenseNo", Reg_licenseNo);//车牌号
		orderRegMap.put("createTime", Reg_date);
		orderRegMap.put("owner", regMap.get("name"));
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, String> sqlMap = (Map<String, String>) AiwbUtils.getBean("Order_SQL");
	
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=OrderRvo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member m = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		//获取jsapp请求数据
		MmbCenterCvo mmbCenterCvo = bsCvo.getBody();
		final MemberExtend extend = bsCvo.getExtend();
		//验证请求类型
		if(!ReqInfoType.CO.toString().equals(mmbCenterCvo.getReqInfoType())){
			bsRvo.setExtend(new MemberExtend(false, "查看订单信息的请求数据类型错误!"));
			return bsRvo;
		}
		
		final MemberExtend memberExtend = bsCvo.getExtend();//获取扩展信息 TODO
		
		String sql="";
		String SQL_Col = sqlMap.get("SQL_Col");
//		String SQL_BRC = map.get("SQL_BaseRelationCondition");
		String search = " "+sqlMap.get("SQL_BaseRelationCondition");
		String SQL_Order = "order by po.\"id\" desc";
		
		
		//订单状态查询
		OrderFilters searchFilter = mmbCenterCvo.getSearchFlag();
		if(!Judgment.nullOrBlank(searchFilter)){
			search += " "+sqlMap.get(searchFilter+"")+" ";
		}
		
		//订单查询条件查询，根据车牌号、车主、投保日期查询
		 String searchEle = mmbCenterCvo.getOrderSearchEle();
		 if(!Judgment.nullOrBlank(searchEle)){
////			 for(String v : orderRegMap.keySet()){
//				 if(searchEle.matches(orderRegMap.get(v))){
//					 search += " and \""+v+"\" like '%"+searchEle+"%' ";
//					 break;
//				 }
				 if(searchEle.length()>=10 && searchEle.substring(0, 10).matches(Reg_date)){
					 search += " and po.\"createTime\" like '"+searchEle.substring(0,10)+"%' ";
//					 break;
				 }else{
					 search += " and (\"owner\" like '%"+searchEle+"%' or \"licenseNo\" like '%"+searchEle.toUpperCase()+"%')";
				 }
//			 }
		 }
		 
		 sql = SQL_Col+search+SQL_Order;
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		params.put("openId", m.getUsername());
		DbCvo<Modelable> dbCvo = new DbCvo<Modelable>(sql, params);
		dbCvo.setDoPaging(true);
		dbCvo.setDoCount(true);
		dbCvo.setRowCount(memberExtend.getRowCount());
		dbCvo.setPagination(memberExtend.getPagination());
		DbRvo dbRvo = ContainerFactory.getSession().query(dbCvo);
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();//返回数据的list
		orderInfos = dbRvo.obtainObjects(OrderInfo.class);
		
		
		OrderRvo orderRvo = new OrderRvo();
		orderRvo.setOrders(orderInfos);
		bsRvo.setBody(orderRvo);
		
//		MemberExtend memberExtend = new MemberExtend(false, null);
		extend.setStatus(true);
		extend.setMsg("订单查询完毕");
		extend.setPageCount(AiwbUtils.obtainPageCount(dbRvo.getRecordCount(),extend.getRowCount()));
		extend.setRecordCount(dbRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}
	
}
