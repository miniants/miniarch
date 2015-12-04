package zbh.aias.appservice.service;

import cn.remex.db.rsql.RsqlCore;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import org.springframework.stereotype.Service;

/** 
 * @author liuhengyang 
 * @date 2015-8-29 下午6:20:59
 * @version 版本号码
 * @TODO 描述
 */
@BusinessService
@Service
public class ResetDB {
	///menu Type 对应的功能方法。
	@BusinessService
	public BsRvo execute(BsCvo bsCvo) {
		//1
	///	bsCvo.getA();
		Object a=bsCvo.$V("a");
		System.out.print(a);
		BsRvo b  =new BsRvo(true,"sd","00");
		b.setBody("asdfas");

		boolean restDB ="true".equals(bsCvo.$V("resetDB"));
		RsqlCore.reset(restDB);
		return b ;
	}
}
