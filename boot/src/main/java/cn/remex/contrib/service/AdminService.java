package cn.remex.contrib.service;

import cn.remex.contrib.RemexAdminUtil;
import cn.remex.contrib.appbeans.AdminBsCvo;
import cn.remex.contrib.appbeans.AdminBsRvo;
import cn.remex.contrib.appbeans.DataCvo;
import cn.remex.contrib.appbeans.DataRvo;
import cn.remex.contrib.auth.AuthenticateBtx;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.model.SysCode;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.SysUri;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.model.cert.Organization;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.connection.RDBSpaceConfig;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;

import java.util.HashMap;
import java.util.List;

import static cn.remex.db.sql.WhereRuleOper.eq;

/** 
 * @author liuhengyang 
 * @date 2015-8-29 下午6:20:59
 * @version 版本号码
 * @TODO 描述
 */
@BusinessService
public class AdminService {
//    //初始化项目使用的
//    @BusinessService
//    public BsRvo addDefaultMenu(boolean delold) {
//
//        if(delold){
//            ContainerFactory.getSession().execute("delete from `SysMenu`",new HashMap<>());
//        }
//
//        SysMenu menu = new SysMenu();
//        menu.setOpened(true);
//        menu.setParentFlag(true);
//        menu.setNodeName("root");
//        ContainerFactory.getSession().store(menu);
//        SysMenu root = menu;
//
//        menu = new SysMenu();
//        menu.setParentFlag(true);
//        menu.setIcon("fa-dashboard");
//        menu.setNodeName("系统配置");
//        menu.setSupMenu(root);
//        ContainerFactory.getSession().store(menu);
//        SysMenu xtpz = menu;
//
//        menu = new SysMenu();
//        menu.setIcon("fa-user");
//        menu.setNodeName("用户");
//        menu.setSupMenu(xtpz);
//        ContainerFactory.getSession().store(menu);
//
//        menu = new SysMenu();
//        menu.setIcon("fa-users");
//        menu.setNodeName("角色");
//        menu.setSupMenu(xtpz);
//        ContainerFactory.getSession().store(menu);
//
//        return new BsRvo(true);
//	}

    //系统配置页
    @BusinessService
    public BsRvo resetSysUris() {//重置权限
        AuthenticateBtx.SysUriMapToRole = new HashMap<>();
        AuthenticateBtx.users = new HashMap<>();
        List<SysUri> sysUris = AuthenticateBtx.obtainDefaultUris();
        sysUris.forEach(sysUri -> ContainerFactory.getSession().storeByJpk(sysUri, null, "uri", "URI必须唯一"));
        return new BsRvo(ServiceCode.SUCCESS, "重构默认URI权限成功,系统不会删除手动配置的URI,但会将系统默认的URI信息更新", sysUris, "text_layout", "text");
    }

    @BusinessService
	public BsRvo resetDb(boolean resetDb) {//重构数据库
        RDBManager.reset(resetDb);
        return new BsRvo(ServiceCode.SUCCESS,"OK",resetDb?"重构数据库成功":"清理数据库缓存成功","text_layout","text");
	}
    @BusinessService
	public Object ormBeans(String spaceName) {//重构数据库
        RDBSpaceConfig spaceConfig = RDBManager.getLocalSpaceConfig(Judgment.nullOrBlank(spaceName) ? RDBManager.DEFAULT_SPACE : spaceName);
        return spaceConfig.getOrmBeans();
	}

    //角色页面
    @BusinessService
    public BsRvo rolesHome(AdminBsCvo bsCvo){
        return RemexAdminUtil.obtainAdminRvo(bsCvo, null, null);
    }
    @BusinessService
    public BsRvo roles(DataCvo bsCvo){
        DbCvo<AuthRole> dbCvo = RemexAdminUtil.obtainDbCvo(AuthRole.class, bsCvo).withColumns()
           //.withList(AuthRole::getMenus).withList(AuthRole::getSysUris)
            ;
        DbRvo dbRvo = dbCvo.ready().query();
        return new DataRvo(dbRvo);
    }
    @BusinessService(requestBody = true)
    public BsRvo saveRole(AuthRole role){
        DbRvo dbRvo = ContainerFactory.getSession().store(role);
        return new BsRvo(ServiceCode.SUCCESS,"",role);
    }
    @BusinessService(requestBody = true)
    public BsRvo delRole(AuthRole role){
        Assert.isTrue(!Judgment.nullOrBlank(role.getName()) && !"root,user,admin".contains(role.getName().trim()),ServiceCode.FAIL,"系统核心角色不能删除！");
        DbRvo dbRvo = ContainerFactory.getSession().delete(role);
        return new BsRvo(dbRvo.getEffectRowCount()==1? ServiceCode.SUCCESS: ServiceCode.FAIL,dbRvo.getMsg());
    }
    @BusinessService(requestBody = true)
    public BsRvo roleUris(String pk){
        return new DataRvo(ContainerFactory.getSession().queryByCollectionField(AuthRole.class, AuthRole::getSysUris, pk));
    }
    @BusinessService(requestBody = true)
    public BsRvo roleMenus(String pk){
        return new DataRvo(ContainerFactory.getSession().queryByCollectionField(AuthRole.class, AuthRole::getMenus, pk));
    }
    @BusinessService(requestBody = true)
    public BsRvo roleOrganizations(String pk){
        return new DataRvo(ContainerFactory.getSession().queryByCollectionField(AuthRole.class, AuthRole::getOrganizations, pk));
    }
    @BusinessService
    public BsRvo organizations(DataCvo bsCvo){//所有组织机构
        DbCvo<Organization> dbCvo = RemexAdminUtil.obtainDbCvo(Organization.class, bsCvo);
        return new DataRvo(dbCvo.ready().query());
    }
    @BusinessService
    public BsRvo uris(DataCvo bsCvo){//功能权限
        DbCvo<SysUri> dbCvo = RemexAdminUtil.obtainDbCvo(SysUri.class, bsCvo);
        return new DataRvo(dbCvo.ready().query());
    }

    //用户页面
    @BusinessService
    public BsRvo usersHome(AdminBsCvo bsCvo){
        return RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
    }
    @BusinessService
    public BsRvo users(DataCvo bsCvo){
        DbCvo<AuthUser> dbCvo = RemexAdminUtil.obtainDbCvo(AuthUser.class, bsCvo);
        return new DataRvo(dbCvo/*.rowCount(1000)*/.ready().query());
    }
    @BusinessService
    public BsRvo saveUser(AuthUser user){
        if(Judgment.nullOrBlank(user.getId()))//新用户进行检查
            Assert.isNull(ContainerFactory.getSession().queryBeanByJpk(AuthUser.class, ServiceCode.FAIL, "id","username",user.getUsername()), ServiceCode.FAIL,"该用户已经被注册!");

        DbRvo dbRvo = ContainerFactory.getSession().store(user);
        return new BsRvo(ServiceCode.SUCCESS,"OK",user);
    }
    @BusinessService
	public BsRvo delUser(AuthUser role) {
		AuthUser au = ContainerFactory.getSession().queryBeanById(AuthUser.class, role.getId());
        Assert.isTrue("admin".equals(au.getUsername()), ServiceCode.FAIL,"系统用户不得删除！");
        DbRvo dbRvo = ContainerFactory.getSession().delete(role);
        return new BsRvo(dbRvo.getEffectRowCount()==1? ServiceCode.SUCCESS: ServiceCode.FAIL,dbRvo.getMsg());
	}
    @BusinessService
    public BsRvo rolesOfUser(String pk){
        return new DataRvo(ContainerFactory.getSession().queryByCollectionField(AuthUser.class, AuthUser::getRoles, pk));
    }
	@BusinessService
	public BsRvo editUser(AdminBsCvo bsCvo, BsRvo bsRvo) {
		String id = bsCvo.getPk();
		
		bsRvo = RemexAdminUtil.obtainAdminRvo(bsCvo,null,null);
		Container session = ContainerFactory.getSession();
        List<AuthUser> data = ContainerFactory.getSession().createDbCvo(AuthUser.class).filterBy(AuthUser::getId, eq, id).ready().queryBeans();

		((AdminBsRvo) bsRvo.getBody()).setDatas(data );
		
		return bsRvo;
	}

    //菜单页面
    @BusinessService
    public BsRvo saveMenu(SysMenu menu){
//        if(Judgment.nullOrBlank(menu.getId()))//新用户进行检查
//            Assert.isNull(ContainerFactory.getSession().queryBeanByJpk(AuthUser.class, "id","username",menu.getUsername()),"该用户已经被注册!");

        DbRvo dbRvo = ContainerFactory.getSession().store(menu);
        return new BsRvo(ServiceCode.SUCCESS,"OK",menu);
    }
    @BusinessService
    public BsRvo delMenu(SysMenu menu) {
        DbRvo dbRvo = ContainerFactory.getSession().delete(menu);
        return new BsRvo(dbRvo.getEffectRowCount()==1? ServiceCode.SUCCESS: ServiceCode.FAIL,dbRvo.getMsg());
    }
    @BusinessService
    public BsRvo menus(DataCvo bsCvo){
        DbCvo<SysMenu> dbCvo = bsCvo.obtainDbCvo(SysMenu.class).withColumns()
                .withModel(SysMenu::getSupMenu,s->s.withModel(SysMenu::getSupMenu,s1->s1.withModel(SysMenu::getSupMenu)));
        return new DataRvo(dbCvo.ready().query());
    }

    //数据字典
    @BusinessService
    public DataRvo sysCode(DataCvo dataCvo) {
        DbCvo<SysCode> dbCvo = RemexAdminUtil.obtainDbCvo(SysCode.class, dataCvo).withColumns();
        return new DataRvo(dbCvo.ready().query());
    }
    @BusinessService
    public SysCode saveSysCode(SysCode sysCode) {
        DbRvo dbRvo = ContainerFactory.getSession().store(sysCode);
        return sysCode;
    }
    @BusinessService
    public BsRvo delSysCode(SysCode sysCode) {
        DbRvo dbRvo = ContainerFactory.getSession().delete(sysCode);
        return new BsRvo(dbRvo.getEffectRowCount() == 1? ServiceCode.SUCCESS: ServiceCode.FAIL, dbRvo.getMsg());
    }

}
