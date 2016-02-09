package cn.remex.admin.service;

import cn.remex.admin.RemexAdminUtil;
import cn.remex.admin.appbeans.AdminBsCvo;
import cn.remex.admin.appbeans.AdminBsRvo;
import cn.remex.admin.appbeans.DataCvo;
import cn.remex.admin.appbeans.DataRvo;
import cn.remex.admin.auth.AuthenticateBtx;
import cn.remex.core.CoreSvo;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.db.*;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.SysUri;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.model.log.LogonLogMsg;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlCore;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Sort;
import cn.remex.db.sql.SqlColumn;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.remex.db.sql.WhereRuleOper.eq;

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
        menu.setSupMenu(root);
        ContainerFactory.getSession().store(menu);
        SysMenu xtpz = menu;

        menu = new SysMenu();
        menu.setIcon("fa-user");
        menu.setNodeName("用户");
        menu.setSupMenu(xtpz);
        ContainerFactory.getSession().store(menu);

        menu = new SysMenu();
        menu.setIcon("fa-users");
        menu.setNodeName("角色");
        menu.setSupMenu(xtpz);
        ContainerFactory.getSession().store(menu);

        return new BsRvo(true);
	}

    //系统初始化相关的方法
    @BusinessService
    public BsRvo resetSysUris() {
        AuthenticateBtx.SysUriMapToRole = new HashMap<>();
        List<SysUri> sysUris = AuthenticateBtx.obtainDefaultUris();
        sysUris.forEach(sysUri -> ContainerFactory.getSession().storeByJpk(sysUri, null, "uri", "URI必须唯一"));
        return new BsRvo(true, "重构默认URI权限成功,系统不会删除手动配置的URI,但会将系统默认的URI信息更新", "CODE01", sysUris, "text_layout", "text");
    }

	///menu Type 对应的功能方法。
	@BusinessService
	public BsRvo resetDb(boolean resetDb) {
        RsqlCore.reset(resetDb);
        return new BsRvo(true,"OK","CODE01",resetDb?"重构数据库成功":"清理数据库缓存成功","text_layout","text");
	}



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
        DbCvo<AuthRole> dbCvo = RemexAdminUtil.obtainDbCvo(AuthRole.class, bsCvo).withColumns()
           //.withList(AuthRole::getMenus).withList(AuthRole::getSysUris)
            ;
        DbRvo dbRvo = dbCvo.ready().query();
        return new DataRvo(true ,dbRvo);
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
        return new DataRvo(true ,ContainerFactory.getSession().queryByCollectionField(AuthRole.class, AuthRole::getSysUris, pk));
    }
    @BusinessService(requestBody = true)
    public BsRvo roleMenus(String pk){
        return new DataRvo(true ,ContainerFactory.getSession().queryByCollectionField(AuthRole.class, AuthRole::getMenus, pk));
    }
    //功能权限
    @BusinessService
    public BsRvo uris(DataCvo bsCvo){
        DbCvo<SysUri> dbCvo = RemexAdminUtil.obtainDbCvo(SysUri.class, bsCvo);
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
        return new DataRvo(true ,dbCvo/*.rowCount(1000)*/.ready().query());
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
    public BsRvo saveMenu(SysMenu menu){
//        if(Judgment.nullOrBlank(menu.getId()))//新用户进行检查
//            Assert.isNull(ContainerFactory.getSession().queryBeanByJpk(AuthUser.class, "id","username",menu.getUsername()),"该用户已经被注册!");

        DbRvo dbRvo = ContainerFactory.getSession().store(menu);
        return new BsRvo(true,menu);
    }
    @BusinessService
    public BsRvo delMenu(SysMenu menu) {
        DbRvo dbRvo = ContainerFactory.getSession().delete(menu);
        return new BsRvo(dbRvo.getEffectRowCount()==1,dbRvo.getMsg());
    }

    @BusinessService
    public BsRvo menus(DataCvo bsCvo){
        DbCvo<SysMenu> dbCvo = RemexAdminUtil.obtainDbCvo(SysMenu.class, bsCvo).withColumns()
                .withModel(SysMenu::getSupMenu);
        return new DataRvo(true ,dbCvo.ready().query());
    }
    @BusinessService
    public BsRvo homeMenus(String rootMenu) {
        //获取三层菜单返回给前端
        DbCvo<SysMenu> dbCvo = ContainerFactory.getSession().createDbCvo(SysMenu.class)
                .orderBy(SysMenu::getNodeOrder, Sort.ASC)
                .withModel(SysMenu::getSupMenu, c -> c.withColumns(ModelableImpl::getId)
                        .filterBy(SysMenu::getNodeName, eq, Judgment.nullOrBlank(rootMenu) ? "ROOTMENU" : rootMenu))
                .withColumns().withList(SysMenu::getSubMenus, sm->sm.withColumns().withList(SysMenu::getSubMenus, SqlColumn::withColumns))
                .rowCount(1000);
        DbRvo dbRvo = dbCvo.ready().query();
        return new DataRvo(true, dbRvo);
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

    @BusinessService
    public BsRvo userProfile() {
        AuthUser curUser = (AuthUser) ContainerFactory.createDbCvo(AuthUser.class)
                .filterBy(AuthUser::getUsername, eq, CoreSvo.valCookieValue("UID"))
                .withList(AuthUser::getRoles, l -> l.withList(AuthRole::getMenus)).rowCount(1000)
                .ready().query().obtainObjects().get(0);

        Map<String, SysMenu> menus = new HashMap<>();
        curUser.getRoles().forEach(
                r-> {
                    if(r.getMenus()!=null)
                    r.getMenus().forEach(
                            m -> menus.put(m.getId(), m));
                });

        Map profile = new HashMap<>();
        profile.put("menus", menus);

        return new BsRvo(true, profile);
    }
    @BusinessService
    public BsRvo logout(String username, String password,String redirect) {
        AuthenticateBtx.clearToken();
        return new BsRvo(true, "退出登录", "0000",null,"/static/framework/login.html","redirect");
    }

    @BusinessService
    public BsRvo login(String username, String password,String redirect) {

        if(Judgment.nullOrBlank(username) || Judgment.nullOrBlank(password))
            return new BsRvo(false,"用户名/密码不能为空!","10000");

        DbRvo dbRvo = ContainerFactory.createDbCvo(AuthUser.class)
                .filterBy(AuthUser::getUsername, eq, username)
                .filterBy(AuthUser::getPassword, eq, password)
                .ready().query()
        ;

        if(dbRvo.getRecordCount()==1) {
            AuthenticateBtx.placeToken(username);
            return new BsRvo(true,"登录成功","00000",redirect);
        }else if (dbRvo.getRecordCount() > 1) {
            AuthenticateBtx.clearToken();
            return new BsRvo(false,"账号异常","10000");
        }else {
            AuthenticateBtx.clearToken();
            return new BsRvo(false,"用户名/密码错误","10000");
        }
    }
}
