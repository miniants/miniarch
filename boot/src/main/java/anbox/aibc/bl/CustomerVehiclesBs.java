package anbox.aibc.bl;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.mmbCenter.CustomerVehicleRvo;
import anbox.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.appBeans.mmbCenter.VehicleInfo;
import anbox.aibc.model.member.CustomerVehicle;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.reflect.ReflectUtil;
import cn.remex.util.Judgment;

/**客户车辆
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class CustomerVehiclesBs  implements AiwbConsts{
	
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=CustomerVehicleRvo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		//获取jsapp请求数据
		MmbCenterCvo mmbCenterCvo = bsCvo.getBody();
		final MemberExtend extend = bsCvo.getExtend();
		
		//验证请求类型
		if(!ReqInfoType.CV.toString().equals(mmbCenterCvo.getReqInfoType())){
			bsRvo.setExtend(new MemberExtend(false, "查看车辆信息的请求数据类型错误!"));
			return bsRvo;
		}
		
//		final MemberExtend memberExtend = bsCvo.getExtend();//获取扩展信息 TODO
		List<VehicleInfo> vehicleInfos = new ArrayList<VehicleInfo>();//rvo的list车辆信息
		//数据库查询用户车辆 TODO
		DbRvo dbRvo = ContainerFactory.getSession().query(
				new DbCvo<CustomerVehicle>(CustomerVehicle.class){
					private static final long serialVersionUID = 7204273675550902536L;
					@Override
					public void initRules(CustomerVehicle t) {//会员数据权限查询条件
						setDoCount(true);
						setDoPaging(true);
						setRowCount(extend.getRowCount()<20?extend.getRowCount():20);
						setPagination(extend.getPagination()<1?0:extend.getPagination());
						if(null !=extend.getFilters()){
							setSqlBeanWhere(extend.getFilters());
						}
						addRule(t.getOpenId(), WhereRuleOper.eq, uid);
						addRule(t.getDisplayStatus(), WhereRuleOper.eq, DisplayStatus_Y);
					}
			
		});
		List<CustomerVehicle> cVehicles = dbRvo.obtainBeans();
		for(CustomerVehicle customerVehicle : cVehicles){
			VehicleInfo vehicleInfo = new VehicleInfo();
			ReflectUtil.copyProperties(vehicleInfo,customerVehicle);
			vehicleInfo.setVehicleId(customerVehicle.getId());//用于前端详情展示
			vehicleInfos.add(vehicleInfo);
		}
		CustomerVehicleRvo customerVehicleRvo = new CustomerVehicleRvo();
		customerVehicleRvo.setVehicleInfos(vehicleInfos);
		bsRvo.setBody(customerVehicleRvo);
//		MemberExtend memberExtend = new MemberExtend(false, null);
		extend.setStatus(true);
		extend.setMsg("车辆信息查询完毕!");
		extend.setPageCount(AiwbUtils.obtainPageCount(dbRvo.getRecordCount(),extend.getRowCount()));
		extend.setRecordCount(dbRvo.getRecordCount());
		bsRvo.setExtend(extend);
		return bsRvo;
	}
	//编辑车辆信息
		@BsAnnotation(bsCvoBodyClass=VehicleInfo.class,
						bsRvoExtendClass=MemberExtend.class)
		public BsRvo editCustomerVehicle(BsCvo bsCvo,BsRvo bsRvo){
			//获取当前session用户
			final String uid = AiwbUtils.getSessionMemberId();
			if(Judgment.nullOrBlank(uid)){
				bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
				return bsRvo;
			}
			
			VehicleInfo vehicleInfo = bsCvo.getBody();
			final String vehicleId = vehicleInfo.getVehicleId();
			
			if(Judgment.nullOrBlank(vehicleId)){
				bsRvo.setExtend(new MemberExtend(false,"cheicleId为空!"));
				return bsRvo;
			}
			
			DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<CustomerVehicle>(CustomerVehicle.class){
				private static final long serialVersionUID = 5710634503102519495L;

				@Override
				public void initRules(CustomerVehicle t) {
					addRule(t.getId(), WhereRuleOper.eq, vehicleId);
					addRule(t.getOpenId(), WhereRuleOper.eq, uid);
				}
			});
			if(dbRvo.getRecordCount()!=1){
				bsRvo.setExtend(new MemberExtend(false,"未查询到相关数据!"));
				return bsRvo;
			}
			
			List<CustomerVehicle> customerVehicles = dbRvo.obtainBeans();
			CustomerVehicle customerVehicle = customerVehicles.get(0);
			
			ReflectUtil.copyProperties(customerVehicle, vehicleInfo);
			ContainerFactory.getSession().store(customerVehicle,"frameNo;engineNo;brandName;owner;ownerCertNo;ownerMobile;enrollDate;seatCount");
			bsRvo.setExtend(new MemberExtend(true,"会员资料更新成功!"));
			return bsRvo;
		}
	
		//保存车辆信息
		@BsAnnotation(bsCvoBodyClass=VehicleInfo.class,
				bsRvoExtendClass=MemberExtend.class)
		public BsRvo addCustomerVehicle(BsCvo bsCvo,BsRvo bsRvo){
			//获取当前session用户
			final String uid = AiwbUtils.getSessionMemberId();
			if(Judgment.nullOrBlank(uid)){
				bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
				return bsRvo;
			}
			
			VehicleInfo vehicleInfo = bsCvo.getBody();
			final String frameNo = vehicleInfo.getFrameNo();
			final String engineNo = vehicleInfo.getEngineNo();
			
			DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<CustomerVehicle>(CustomerVehicle.class){
				private static final long serialVersionUID = 5710634503102519495L;
				
				@Override
				public void initRules(CustomerVehicle t) {
					addRule(t.getOpenId(), WhereRuleOper.eq, uid);
					addRule(t.getFrameNo(), WhereRuleOper.eq, frameNo);
					addRule(t.getEngineNo(), WhereRuleOper.eq, engineNo);
				}
			});
			CustomerVehicle vehicle;
			if(dbRvo.getRecordCount()>=1){
				vehicle = (CustomerVehicle) dbRvo.obtainBeans().get(0);
				ReflectUtil.copyProperties(vehicle, vehicleInfo);
				ContainerFactory.getSession().store(vehicle);
			}else{
				CustomerVehicle customerVehicle = new CustomerVehicle();
				ReflectUtil.copyProperties(customerVehicle, vehicleInfo);
				customerVehicle.setOpenId(uid);
				customerVehicle.setDisplayStatus(DisplayStatus_Y);
				ContainerFactory.getSession().store(customerVehicle);
			}
			bsRvo.setExtend(new MemberExtend(true,"更新成功!"));
			return bsRvo;
		}
		
}
