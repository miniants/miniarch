package zbh.aibc.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import anbox.aibc.appBeans.mmbCenter.DeliveryEditCvo;
import anbox.aibc.appBeans.mmbCenter.DeliveryRvo;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.model.member.DeliveryInfo;
import zbh.aias.model.member.Member;
import zbh.aibc.appBeans.mmbCenter.DeliveryInfo;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;
import zbh.remex.util.Judgment;

/**会员递送信息管理
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class MmbDeliveryBs  implements AiwbConsts{
	//获取会员递送信息
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=DeliveryRvo.class,
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
		
		//验证请求类型
		if(!ReqInfoType.MD_View.toString().equals(mmbCenterCvo.getReqInfoType())){
			bsRvo.setExtend(new MemberExtend(false, "查看递送信息的请求数据类型错误!"));
			return bsRvo;
		}
		
		//查询会员的递送信息
		List<DeliveryInfo> deliveryInfos = ContainerFactory.getSession().query(
				new DbCvo<DeliveryInfo>(DeliveryInfo.class){
					private static final long serialVersionUID = -8622419602237050312L;
					@Override
					public void initRules(DeliveryInfo t) {
						setDoCount(true);setDoPaging(true);setRowCount(10);setPagination(1); // 分页参数 TODO
						addRule(t.getOpenId(), WhereRuleOper.eq, m.getUsername());
					}
				}).obtainBeans();
		
		List<DeliveryInfo> dis = new ArrayList<DeliveryInfo>();
		for(DeliveryInfo di:deliveryInfos){
			DeliveryInfo deliveryInfo = new DeliveryInfo();
			ReflectUtil.copyProperties(deliveryInfo, di);
			dis.add(deliveryInfo);
		}
		
		DeliveryRvo deliveryRvo = new DeliveryRvo();
		deliveryRvo.setDeliveryInfos(dis);
		bsRvo.setBody(deliveryRvo);
		bsRvo.setExtend(new MemberExtend(true,"查询成功"));
		return bsRvo;
	}
	
	//编辑会员递送信息
	@BsAnnotation(bsCvoBodyClass=DeliveryEditCvo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo editDelivery(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member m = AiwbUtils.getLoginedMember();
		if(Judgment.nullOrBlank(m)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		DeliveryEditCvo deCvo = bsCvo.getBody();
		DeliveryInfo deliveryInfo = null;
		
		if(!Judgment.nullOrBlank(deCvo.getId())){
			deliveryInfo = ContainerFactory.getSession().queryBeanById(DeliveryInfo.class, deCvo.getId());
			if(null == deliveryInfo){
				bsRvo.setExtend(new MemberExtend(false, "没有查到符合条件的数据"));
				return bsRvo;
			}
			if(!m.getUsername().equals(deliveryInfo.getOpenId())){
				bsRvo.setExtend(new MemberExtend(false, "权限不足!"));
				return bsRvo;
			}
		}else{
			deliveryInfo = new DeliveryInfo();
			deliveryInfo.setDefaultFlag("N");
		}
		deliveryInfo.setOpenId(m.getUsername());
		deliveryInfo.setMember(m);
		ReflectUtil.copyProperties(deliveryInfo, deCvo);
		//更新递送信息
		if(Flag.Y.toString().equals(deCvo.getDefaultFlag())){
			deliveryInfo.setDefaultFlag("Y");
			String sql = "update \"DeliveryInfo\" set \"defaultFlag\"='N' where \"openId\"=:openId" ;
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("openId", m.getUsername());
			ContainerFactory.getSession().executeUpdate(sql, p);
		}
		ContainerFactory.getSession().store(deliveryInfo);
		bsRvo.setExtend(new MemberExtend(true,"更新成功!"));
		return bsRvo;
	}
}
