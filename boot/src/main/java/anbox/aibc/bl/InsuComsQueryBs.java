package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.appBeans.autoInsu.AutoInsuExtend;
import anbox.aibc.appBeans.autoInsu.AutoInsuInfoCvo;
import anbox.aibc.appBeans.autoInsu.AutoInsuInfoRvo;
import cn.com.qqbx.aitp.aiws.xmlbeans.autoinsuinfo.ReqAutoInsuInfo;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;

/**保险公司查询
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class InsuComsQueryBs implements AiwbConsts{
	@BsAnnotation(bsCvoBodyClass=AutoInsuInfoCvo.class,bsRvoBodyClass = AutoInsuInfoRvo.class,
			bsCvoExtendClass=AutoInsuExtend.class,bsRvoExtendClass=AutoInsuExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		AutoInsuInfoCvo aiCvo = bsCvo.getBody();
		ReqAutoInsuInfo reqAutoInsuInfo = new ReqAutoInsuInfo();
		reqAutoInsuInfo.setCityCode(aiCvo.getCityCode());
		//TODO 获取登录用户 reqAutoInsuInfo.set
//		ResAutoInsuInfo resAutoInsuInfo = AiwbUtils.invokeService(AitpBsNames.AitpAutoInsuInfoBs, TransType.queryInsuComs, reqAutoInsuInfo, TransChannel_web,"001");
		AutoInsuInfoRvo aiRvo = null;
		bsRvo.setBody(aiRvo);
		return bsRvo;
	}
}
