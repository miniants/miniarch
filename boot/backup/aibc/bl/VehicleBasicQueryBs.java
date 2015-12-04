package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import anbox.aibc.appBeans.quotation.QuotationExtend;
import anbox.aibc.appBeans.vehicle.VehicleBasicCvo;
import anbox.aibc.appBeans.vehicle.VehicleBasicRvo;
import anbox.aibc.appBeans.vehicle.VehicleQueryExtend;
import anbox.aibc.model.member.CustomerVehicle;
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

/**车辆基本信息查询
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class VehicleBasicQueryBs implements AiwbConsts {
	@BsAnnotation(bsCvoBodyClass=VehicleBasicCvo.class,bsRvoBodyClass=VehicleBasicRvo.class,
			bsCvoExtendClass=VehicleQueryExtend.class,bsRvoExtendClass=VehicleQueryExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		final VehicleBasicCvo vbCvo = bsCvo.getBody();
		
		if("1".equals(vbCvo.getNewVehicleFlag())){//新车直接返回
			bsRvo.setExtend(new QuotationExtend(false, "新车"));
			return bsRvo;
		}
		
		//车牌不能为空
		if(Judgment.nullOrBlank(vbCvo.getLicenseNo())){//新车直接返回
			bsRvo.setExtend(new QuotationExtend(false, "旧车车牌不能为空"));
			return bsRvo;
		}
		
		vbCvo.setNewVehicleFlag("0");
		VehicleBasicRvo vbRvo = new VehicleBasicRvo();
		
		//如果没有登录会自动添加一个临时用户
		AiwbUtils.setSessionGuestTempId();
		
		final String uid =AiwbUtils.getSessionMemberId();
		
		//先查本库
		DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<CustomerVehicle>(CustomerVehicle.class){
			private static final long serialVersionUID = 1865012222355749887L;
			@Override
			public void initRules(CustomerVehicle t) {
				setDoCount(true);setDoPaging(true);setRowCount(10);setPagination(1); // 分页参数 TODO
				addRule(t.getLicenseNo(), WhereRuleOper.eq, vbCvo.getLicenseNo());
				addRule(t.getOpenId(), WhereRuleOper.eq, uid);
			}
		});
		CustomerVehicle vehicle;
		if(dbRvo.getRecords()>=1){//TODO 为什么直接获取第一个
			vehicle = (CustomerVehicle) dbRvo.obtainBeans().get(0);
			ReflectUtil.copyProperties(vbRvo, vehicle);
			vehicle.setDisplayStatus(DisplayStatus_Y);
		}else{
			vehicle = new CustomerVehicle();
			vehicle.setDisplayStatus(DisplayStatus_Y);
		}
		
		//根据情况调用服务。
		if(dbRvo.getRecords()==0 || (dbRvo.getRecords()>=1 && Judgment.nullOrBlank(vehicle.getEngineNo()) && Judgment.nullOrBlank(vehicle.getFrameNo()))){
			ReqVehicleBase reqVehicleBase = new ReqVehicleBase();
			ReflectUtil.copyProperties(reqVehicleBase, vbCvo);
			ResVehicleBase resVehicleBase;
			try{
				resVehicleBase =(ResVehicleBase) AiwbUtils.invokeService("AitpVehicleBaseBs", "execute", reqVehicleBase,null, "ZHDX","");
			} catch (Exception e) {
				bsRvo.setExtend(new QuotationExtend(false, AiwbUtils.transErrorMsg(e.getMessage())));
				return bsRvo;
			}
			ReflectUtil.copyProperties(vbRvo, resVehicleBase);
//			Member m = AiwbUtils.getSessionMember();
//			if(null !=m.getId()){
//				vehicle.setMember(m);
//			}
		}
		
		if(vbRvo.getEngineNo()==null || vbRvo.getFrameNo()==null){
			bsRvo.setExtend(new VehicleQueryExtend(false, "false"));
			bsRvo.setBody(vbRvo);
		}else{
			ReflectUtil.copyProperties(vehicle,vbCvo);
			//存本地。
			ReflectUtil.copyProperties(vehicle,vbRvo,SPFeature.IgnoreEmptyStringValue);
			vehicle.setOpenId(uid);//暂时默认这个字段保存用户id，不建议保存到member去
			ContainerFactory.getSession().store(vehicle);
			bsRvo.setExtend(new VehicleQueryExtend(true, "OK"));
			bsRvo.setBody(vbRvo);
		}
		return bsRvo;
	}
}
