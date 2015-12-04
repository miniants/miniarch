package zbh.aibc.bl;


import anbox.aibc.AiwbConsts;
import anbox.aibc.AiwbUtils;
import zbh.aibc.appBeans.mmbCenter.MemberExtend;
import anbox.aibc.appBeans.mmbCenter.MmbCenterCvo;
import anbox.aibc.appBeans.mmbCenter.PersonalCvo;
import anbox.aibc.appBeans.mmbCenter.PersonalRvo;
import zbh.aias.model.member.Member;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import zbh.remex.reflect.ReflectUtil;

/**个人资料
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class PersonalBs  implements AiwbConsts{
	//获取会员个人资料
	@BsAnnotation(bsCvoBodyClass=MmbCenterCvo.class,bsRvoBodyClass=PersonalRvo.class,
			bsRvoExtendClass=MemberExtend.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		//获取jsapp请求数据
		MmbCenterCvo mmbCenterCvo = bsCvo.getBody();
		//验证请求数据类型的合法性
		if(!ReqInfoType.PS_View.toString().equals(mmbCenterCvo.getReqInfoType())){
			bsRvo.setExtend(new MemberExtend(false, "请求查看的数据类型错误!"));
			return bsRvo;
		}
		
		PersonalRvo personalRvo = new PersonalRvo();
		ReflectUtil.copyProperties(personalRvo, curMember);
		bsRvo.setBody(personalRvo);
		bsRvo.setExtend(new MemberExtend(true,"查询完毕!"));
		return bsRvo;
		
	}

	//编辑会员个人资料
	@BsAnnotation(bsCvoBodyClass=PersonalCvo.class,
			bsRvoExtendClass=MemberExtend.class)
	public BsRvo editPersonal(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
				
		PersonalCvo personalCvo = bsCvo.getBody();
		String un = curMember.getUsername();
		ReflectUtil.copyProperties(curMember, personalCvo);//将页面修改后传入的信息复制到member数据库表中
		curMember.setUsername(un);
		ContainerFactory.getSession().store(curMember, new DbCvo<Member>(Member.class){
			private static final long serialVersionUID = 7248900680696399210L;
			@Override
			public void setDataType(String dataType) {
				setDataType("bd");
			}
		});
		bsRvo.setExtend(new MemberExtend(true,"会员资料更新成功!"));
		return bsRvo;
	}
	@BsAnnotation(bsCvoBodyClass=PersonalCvo.class,
			bsRvoExtendClass=MemberExtend.class)
	public BsRvo upgrade(BsCvo bsCvo,BsRvo bsRvo){
		//获取当前session用户
		final Member curMember = AiwbUtils.getLoginedMember();
		if(null == curMember){
			bsRvo.setExtend(new MemberExtend(false, TipsMsg_noLogin));
			return bsRvo;
		}
		
		PersonalCvo personalCvo = bsCvo.getBody();
		ReflectUtil.copyProperties(curMember, personalCvo);//将页面修改后传入的信息复制到member数据库表中
		curMember.setMemberType(MemberType.Business.toString());
		curMember.setLevel(AiwbConsts.memberLevel.Small.toString());
		ContainerFactory.getSession().store(curMember, new DbCvo<Member>(Member.class){
			private static final long serialVersionUID = 7248900680696399210L;
			@Override
			public void setDataType(String dataType) {
				setDataType("bd");
			}
		});
		bsRvo.setExtend(new MemberExtend(true,"会员升级成功!"));
		return bsRvo;
	}
	
}
