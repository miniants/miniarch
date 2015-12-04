package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import zbh.aibc.appBeans.autoInsu.AutoInsuExtend;
import anbox.aibc.appBeans.autoInsu.AutoInsuInfoCvo;
import anbox.aibc.appBeans.autoInsu.AutoInsuInfoRvo;
import zbh.aibc.appBeans.autoInsu.AutoInsuExtend;
import zbh.com.qqbx.aitp.aiws.xmlbeans.autoinsuinfo.ReqAutoInsuInfo;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;

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
