package cn.remex.admin.service;

import cn.remex.admin.RemexAdminUtil;
import cn.remex.admin.appbeans.AdminBsCvo;
import cn.remex.admin.appbeans.AdminBsRvo;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Judgment;
import cn.remex.db.*;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.model.log.LogonLogMsg;
import cn.remex.db.sql.WhereRuleOper;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 
 * @author liuhengyang 
 * @date 2015-8-29 下午6:20:59
 * @version 版本号码
 * @TODO 描述
 */
@BusinessService
@Service
public class AdminBs {
	public BsRvo addDefaultMenu(boolean delold) {

        if(delold){
            ContainerFactory.getSession().execute("delete from `SysMenu`",new HashMap<>());
        }

        SysMenu menu = new SysMenu();
        menu.setOpened(true);
        menu.setParentFlag(true);
        menu.setNodeName("root");
        ContainerFactory.getSession().store(menu);
        SysMenu root = menu;

        menu = new SysMenu();
        menu.setParentFlag(true);
        menu.setIcon("fa-dashboard");
        menu.setNodeName("系统配置");
        menu.setParent(root);
        ContainerFactory.getSession().store(menu);
        SysMenu xtpz = menu;

        menu = new SysMenu();
        menu.setIcon("fa-user");
        menu.setNodeName("用户");
        menu.setParent(xtpz);
        ContainerFactory.getSession().store(menu);

        menu = new SysMenu();
        menu.setIcon("fa-users");
        menu.setNodeName("角色");
        menu.setParent(xtpz);
        ContainerFactory.getSession().store(menu);

        return new BsRvo(true);
	}

	///menu Type 对应的功能方法。
	@BusinessService
	public BsRvo execute(AdminBsCvo bsCvo) {
//		//1
//		ContainerFactory.createSession(SysMenu.class, (d, t) -> {
//            d.putRule(t.getIcon(), WhereRuleOper.eq, "");
//            d.putRule(t.getIcon(), WhereRuleOper.eq, "");
//        });
//		//2
//        DbCvo<SysMenu> dbCvo = ContainerFactory.createDbCvo(SysMenu.class)
//               // .where(SysMenu::getName, WhereRuleOper.eq, "根菜单")
//                .dataColumns(SysMenu::getIcon, SysMenu::getParent);
//        //3

//        List<SysMenu> sysMenus = ContainerFactory.createDbCvo(SysMenu.class).filterBy(SysMenu::getParent,WhereRuleOper.eq,"root")
//                .ready().query().obtainBeans();

        return RemexAdminUtil.obtainAdminRvo(bsCvo,"index");
	}
	@BusinessService
	public BsRvo confSysMenu(AdminBsCvo bsCvo) {
		return RemexAdminUtil.obtainAdminRvo(bsCvo, "/admin/admin");
	}
	@BusinessService
	public BsRvo delUser(AdminBsCvo bsCvo) {
		String id = bsCvo.getPk();

		AuthUser au = ContainerFactory.getSession().queryBeanById(AuthUser.class, id);
		if("admin".equals(au.getUsername())){
			throw new RuntimeException("系统用户不得删除！");
		}
		ContainerFactory.getSession().deleteById(AuthUser.class, id);

		return listUser(bsCvo,new AdminBsRvo());
	}
	@BusinessService
	public BsRvo editUser(AdminBsCvo bsCvo,BsRvo bsRvo) {
		String id = bsCvo.getPk();
		
		bsRvo = RemexAdminUtil.obtainAdminRvo(bsCvo,"/admin/editUser");
		Container session = ContainerFactory.getSession();
		
		List<?> data = session.queryById(AuthUser.class, id).obtainObjects(AuthUser.class);
		if(data.size()>0){
			//Article r = (Article) data.get(0);
//			r.setEduBackgrounds(session.queryByFields(EduBackground.class, null, "researcher", r.getId()).obtainObjects(EduBackground.class));
		}
		
		((AdminBsRvo) bsRvo.getBody()).setDatas(data );
		
		return bsRvo;
	}
	@BusinessService
	public BsRvo listUser(BsCvo bsCvo,BsRvo bsRvo) {
//		bsRvo = RemexAdminUtil.obtainAdminRvo(bsRvo, AdminBsRvo.class, "/admin/listUser");
//		//TODO 查询当前用户的用户列表，id获取错误
//		String id = CoreSvo.$SV("RMX_USERID");
//		DbRvo dbRvo = RemexAdminUtil.obtainDbCvo(AuthUser.class, bsCvo)
//				.putRule("id", WhereRuleOper.eq, id)
//				.ready().query();
//		//管理员账号可以查看所有用户
//		AuthUser au = ContainerFactory.getSession().queryBeanById(AuthUser.class, id);
//		if("admin".equals(au.getUsername())){
//			dbRvo = RemexAdminUtil.obtainDbCvo(AuthUser.class, bsCvo)
//					.ready().query();
//		}
//
//		((AdminBsRvo) bsRvo.getBody()).setDatas(dbRvo.obtainObjects(AuthUser.class));
//		RemexAdminUtil.saveDataMeta(bsRvo, dbRvo);
		
		return bsRvo;
	}
	@BusinessService
	public BsRvo listLogonLogMsg(AdminBsCvo bsCvo) {
		//String id = bsCvo.getHead().getResources();
		BsRvo bsRvo = RemexAdminUtil.obtainAdminRvo(bsCvo, "/admin/listLogonLogMsg");
		
		DbRvo dbRvo = RemexAdminUtil.obtainDbCvo(LogonLogMsg.class, bsCvo).ready().query();
		
		((AdminBsRvo) bsRvo.getBody()).setDatas(dbRvo.obtainObjects(LogonLogMsg.class));
		RemexAdminUtil.saveDataMeta(bsRvo, dbRvo);
		
		return bsRvo;
	}
	@BusinessService
	public BsRvo saveUser(AdminBsCvo bsCvo) {
		String id = bsCvo.getPk();
		AuthUser r = null;
		if(Judgment.notEmpty(id))
			r = ContainerFactory.getSession().queryBeanById(AuthUser.class, id);
		if(r == null)
			r = new AuthUser();
		
		AuthUser body = bsCvo.getUser();
		
		ReflectUtil.copyProperties(r, body);
		r.setUsername(r.getName());
		
		ContainerFactory.getSession().createDbCvo(AuthUser.class).putDataType("bdod").ready().store(r);
		
		return new BsRvo();
	}
}
