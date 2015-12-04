package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import anbox.aibc.appBeans.vehicle.VehicleBasicCvo;
import anbox.aibc.appBeans.vehicle.VehicleBasicRvo;
import anbox.aibc.appBeans.vehicle.VehicleQueryExtend;
import anbox.aibc.model.member.CustomerVehicle;
import zbh.aibc.AiwbUtils;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.reflect.ReflectUtil;

/**根据车牌号的改变进行查询，已减少客户输入
 * @author zhangaiguo
 *@since 2015-02-04
 */
@BsAnnotation
public class VBFastQueryBs implements AiwbConsts {
	@BsAnnotation(bsCvoBodyClass=VehicleBasicCvo.class,bsRvoBodyClass=VehicleBasicRvo.class,
			bsCvoExtendClass=VehicleQueryExtend.class,bsRvoExtendClass=VehicleQueryExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		final VehicleBasicCvo vbCvo = bsCvo.getBody();
		VehicleBasicRvo vbRvo = new VehicleBasicRvo();
		
		final String uid = AiwbUtils.getSessionMemberId();
		
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
			bsRvo.setExtend(new VehicleQueryExtend(true, "OK"));
			bsRvo.setBody(vbRvo);
		}else{
			bsRvo.setExtend(new VehicleQueryExtend(false, "false"));
		}
		return bsRvo;
	}
}
