package zbh.aibc.bl;

import java.util.List;

import anbox.aibc.AiwbConsts;
import zbh.aibc.AiwbUtils;
import anbox.aibc.appBeans.mmbCenter.DeliveryRvo;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import zbh.aibc.appBeans.mmbCenter.MmbCenterCvo;
import zbh.aias.model.member.Member;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import zbh.remex.util.Judgment;

@BsAnnotation
public class ObtainDeliverInfoBs  implements AiwbConsts{
	
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=DeliveryRvo.class,
			bsCvoExtendClass=MemberExtend.class,bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		final String uid = AiwbUtils.getSessionMemberId();
		if(Judgment.nullOrBlank(uid)){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		DbRvo dbRvo = ContainerFactory.getSession().query(new DbCvo<Member>(Member.class){
			private static final long serialVersionUID = 1865012222355749887L;
			@Override
			public void initRules(Member t) {
				addRule(t.getUsername(), WhereRuleOper.eq, uid);
			}
		});
		List<Member> member = dbRvo.obtainBeans();
		if(member.size()==1){
			bsRvo.setExtend(new MemberExtend(true, "递送信息查询完毕"));
			bsRvo.setBody(member.get(0));
		}else{
			bsRvo.setExtend(new MemberExtend(false,"error,不存在该用户或用户不唯一"));
		}
		return bsRvo;
	}
}
