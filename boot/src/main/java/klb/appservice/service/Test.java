package klb.appservice.service;

import cn.remex.admin.RemexAdminUtil;
import cn.remex.admin.appbeans.AdminBsCvo;
import cn.remex.admin.appbeans.AdminBsRvo;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Judgment;
import cn.remex.db.*;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.model.log.LogonLogMsg;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

/** 
 * @author liuhengyang 
 * @date 2015-8-29 下午6:20:59
 * @version 版本号码
 * @TODO 描述
 */
@BusinessService
@Service
public class Test {
	///menu Type 对应的功能方法。
	@BusinessService
	public BsRvo execute(BsCvo bsCvo) {
		//1
	///	bsCvo.getA();
		Object a=bsCvo.$V("a");
		System.out.print(a);
		BsRvo b  =new BsRvo(true,"sd","00");
		b.setBody("asdfas");
		return b ;
	}
}
