package cn.remex.admin.service;

import cn.remex.admin.RemexAdminUtil;
import cn.remex.admin.appbeans.AdminBsCvo;
import cn.remex.admin.appbeans.AdminBsRvo;
import cn.remex.admin.appbeans.DataCvo;
import cn.remex.admin.appbeans.DataRvo;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.db.*;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.model.cert.AuthUri;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.model.log.LogonLogMsg;
import cn.remex.db.rsql.RsqlUtils;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;

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
        return RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
	}
    private static boolean all = false;
    @BusinessService
    public BsRvo turnoffAll(AdminBsCvo bsCvo) {
        all=false;
        return new BsRvo(true,"OK","CODE01","01","text_layout","text");
    }
    @BusinessService
    public BsRvo turnonAll(AdminBsCvo bsCvo) {
        all=true;
        return new BsRvo(true,"OK","CODE01","01","text_layout","text");
    }
    @BusinessService
    public BsRvo arduino(AdminBsCvo bsCvo) {
        return new BsRvo(true,"OK","CODE01",all?"01":"00","text_layout","text");
    }
    @BusinessService
    public BsRvo arduinoHome(AdminBsCvo bsCvo) {
        return RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
    }
    @BusinessService
	public BsRvo confSysMenu(AdminBsCvo bsCvo) {
		return RemexAdminUtil.obtainAdminRvo(bsCvo, null,null);
	}
	@BusinessService
	public BsRvo adminIndex(AdminBsCvo bsCvo){
		return RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
	}

    //角色
    @BusinessService
    public BsRvo rolesHome(AdminBsCvo bsCvo){
        return RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
    }
    @BusinessService
    public BsRvo roles(DataCvo bsCvo){
        DbCvo<AuthRole> dbCvo = RemexAdminUtil.obtainDbCvo(AuthRole.class, bsCvo);
        return new DataRvo(true ,dbCvo.ready().query());
    }
    @BusinessService(requestBody = true)
    public BsRvo saveRole(AuthRole role){
        DbRvo dbRvo = ContainerFactory.getSession().store(role);
        return new BsRvo(true,role);
    }
    @BusinessService(requestBody = true)
    public BsRvo delRole(AuthRole role){
        DbRvo dbRvo = ContainerFactory.getSession().delete(role);
        return new BsRvo(dbRvo.getEffectRowCount()==1,dbRvo.getMsg());
    }
    @BusinessService(requestBody = true)
    public BsRvo roleUris(String pk){
        return new DataRvo(true ,ContainerFactory.getSession().queryByCollectionField(AuthRole.class, AuthRole::getAuthUris, pk));
    }
    //功能权限
    @BusinessService
    public BsRvo uris(DataCvo bsCvo){
        DbCvo<AuthUri> dbCvo = RemexAdminUtil.obtainDbCvo(AuthUri.class, bsCvo);
        return new DataRvo(true ,dbCvo.ready().query());
    }

    //用户
    @BusinessService
    public BsRvo usersHome(AdminBsCvo bsCvo){
        return RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
    }
    @BusinessService
    public BsRvo users(DataCvo bsCvo){
        DbCvo<AuthUser> dbCvo = RemexAdminUtil.obtainDbCvo(AuthUser.class, bsCvo);
        return new DataRvo(true ,dbCvo.putRowCount(1000).ready().query());
    }
    @BusinessService
    public BsRvo saveUser(AuthUser user){
        if(Judgment.nullOrBlank(user.getId()))//新用户进行检查
            Assert.isNull(ContainerFactory.getSession().queryBeanByJpk(AuthUser.class, "id","username",user.getUsername()),"该用户已经被注册!");

        DbRvo dbRvo = ContainerFactory.getSession().store(user);
        return new BsRvo(true,user);
    }
    @BusinessService
	public BsRvo delUser(AuthUser role) {
		AuthUser au = ContainerFactory.getSession().queryBeanById(AuthUser.class, role.getId());
		if("admin".equals(au.getUsername())){
            return new BsRvo(false,"系统用户不得删除！","01");
		}
        DbRvo dbRvo = ContainerFactory.getSession().delete(role);
        return new BsRvo(dbRvo.getEffectRowCount()==1,dbRvo.getMsg());
	}
    @BusinessService
    public BsRvo rolesOfUser(String pk){
        return new DataRvo(true ,ContainerFactory.getSession().queryByCollectionField(AuthUser.class, AuthUser::getRoles, pk));
    }
	@BusinessService
	public BsRvo editUser(AdminBsCvo bsCvo,BsRvo bsRvo) {
		String id = bsCvo.getPk();
		
		bsRvo = RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
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

		return bsRvo;
	}
	@BusinessService
	public BsRvo listLogonLogMsg(AdminBsCvo bsCvo) {
		//String id = bsCvo.getHead().getResources();
		BsRvo bsRvo = RemexAdminUtil.obtainAdminRvo(bsCvo, null, null);
		
		DbRvo dbRvo = RemexAdminUtil.obtainDbCvo(LogonLogMsg.class, bsCvo).ready().query();

		((AdminBsRvo) bsRvo.getBody()).setDatas(dbRvo.obtainObjects(LogonLogMsg.class));
//		RemexAdminUtil.saveDataMeta(bsRvo, dbRvo);
		
		return bsRvo;
	}

}
