/**
 * 
 */
package cn.remex.admin;

import cn.remex.admin.appbeans.AdminBsRvo;
import cn.remex.admin.appbeans.DataMeta;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Judgment;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.config.ConfigInfo;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.WhereRuleOper;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author liuhengyang 
 * @date 2015-8-29 下午6:22:25
 * @version 版本号码
 * @TODO 描述
 */
public class RemexAdminUtil {
	private static SysMenu sysMenu;
	/**
	 * @param bsRvo
	 * @param url
	 * @return
	 */
	public static BsRvo obtainAdminRvo(AdminBsRvo bsRvo, String url) {
		AdminBsRvo rvoBody;
		Container s = ContainerFactory.getSession();

		if(null==sysMenu){
			SysMenu l0 = new SysMenu();
			SysMenu sysMenu1 = s .createDbCvo(SysMenu.class).putOrder(true, "nodeOrder", "asc").ready().queryBeanByJpk(SysMenu.class, null, "id", "1");
			ReflectUtil.copyProperties(l0, sysMenu1);
			
			l0.setSubMenus(new ArrayList<SysMenu>());
			List<SysMenu> sm1list = s.createDbCvo(SysMenu.class)
					.putOrder(true, "nodeOrder", "asc").putRule("parent", WhereRuleOper.eq, sysMenu1.getId())
					.ready().query().obtainBeans();
			for(SysMenu sm1:sm1list){
				SysMenu l1 = new SysMenu();
				ReflectUtil.copyProperties(l1, sm1);
				l1.setParent(null);
				l0.getSubMenus().add(l1);
				
				l1.setSubMenus(new ArrayList<SysMenu>());
				List<SysMenu> sm2list = s.createDbCvo(SysMenu.class)
						.putOrder(true, "nodeOrder", "asc").putRule("parent", WhereRuleOper.eq, sm1.getId())
						.ready().query().obtainBeans();
				for(SysMenu sm2:sm2list){
					SysMenu l2 = new SysMenu();
					ReflectUtil.copyProperties(l2, sm2);
					l2.setParent(null);
					l1.getSubMenus().add(l2);
				}
			}
			
			sysMenu = l0;
		}
		bsRvo.setSysMenu(sysMenu);
		bsRvo.setRv(url);
		return bsRvo;
	}
	
	/**
	 * @param clazz
	 * @param bsCvo
	 * @param params 用于程序中强制赋值参数，0为排序
	 * @return
	 */
	public static <T extends ModelableImpl> DbCvo<T> obtainDbCvo(Class<T> clazz,BsCvo bsCvo,String... params){
		
		
		int rc = Integer.parseInt((String) (Judgment.notEmpty(bsCvo.$V("rc"))?bsCvo.$V("rc"):"12"));
		int p = Integer.parseInt((String) (Judgment.notEmpty(bsCvo.$V("p"))?bsCvo.$V("p"):"1"));
		
		//params的第一个参数 或者cvo中的or参数是排序
		String orders = params.length>0?params[0]:((String) (Judgment.notEmpty(bsCvo.$V("or"))?bsCvo.$V("or"):"createTime"));
		//params的第二个参数或者cvo中的ss参数是搜索字符串
		
		DbCvo<T> ret = (DbCvo<T>)ContainerFactory.getSession().createDbCvo(clazz)
				.putRowCount(rc).putPagination(p).putDoCount(true).putDoPaging(true);
		
		for(String or:orders.split(";")){
			String[] orParts = or.split(" ");
			ret.putOrder(true, orParts[0], orParts.length == 1 || Judgment.nullOrBlank(orParts[1])?"DESC":orParts[1]);
		}
		
		return ret;
	}
	public static <T extends ModelableImpl> void saveDataMeta(BsRvo bsRvo,DbRvo dbRvo){
		AdminBsRvo adminBsRvo = (AdminBsRvo) bsRvo.getBody();
		DataMeta meta = new DataMeta();

		meta.setRowCount(12);
		meta.setPagination(dbRvo.getPagination());
		meta.setRecordCount(dbRvo.getRecordCount());
		if(dbRvo.getRowCount() != 0){
			double b = (meta.getRecordCount()*1.00)/meta.getRowCount();
			meta.setPageCount((int)Math.ceil(b));
		}
		adminBsRvo.setDataMeta(meta);
	}
//	private static HashMap<String, String> config = new HashMap<String, String>();
	public static String obtainConfig(String key){
		return obtainConfig("default","default","default","default","default", key);
	}
	public static String obtainConfig(String environment,String confClass,String confKind,String confType,String group,String key){
		ConfigInfo conf = ContainerFactory.getSession().queryBeanByJpk(ConfigInfo.class, null, "environment;confClass;confKind;confType;group;key", environment,confClass,confKind,confType,confType,key);
		return conf.getValue();
	}

	public static void saveConfig(String key,String value){
		saveConfig("default","default","default","default","default", key,value);
	}
	public static void saveConfig(String environment,String confClass,String confKind,String confType,String group,String key,String value){
		ConfigInfo conf = new ConfigInfo();
		conf.setEnvironment(environment);
		conf.setConfClass(confClass);
		conf.setConfKind(confKind);
		conf.setConfType(confType);
		conf.setGroup(group);
		conf.setKey(key);
		conf.setValue(value);
		ContainerFactory.getSession().updateByFields(conf, null, "environment;confClass;confKind;confType;group;key");
	}
}
