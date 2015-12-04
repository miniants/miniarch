package zbh.wx.bs;

import zbh.wx.AiwcConsts;
import zbh.wx.appbeans.wxOauth.WxOauthCvo;
import zbh.wx.appbeans.wxOauth.WxOauthExtend;
import zbh.wx.appbeans.wxOauth.WxOauthRvo;
import zbh.wx.utils.WxUtil;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
import zbh.remex.util.Judgment;

@BsAnnotation()
public class StatisticsBs implements AiwcConsts {
	
	@BsAnnotation(bsCvoBodyClass=WxOauthCvo.class,bsRvoBodyClass=WxOauthRvo.class,
			bsCvoExtendClass=WxOauthExtend.class,bsRvoExtendClass=WxOauthExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		WxOauthCvo wxOauthCvo = bsCvo.getBody();
		
		/********************网页授权获取用户信息部分****************************/
		
		String code = wxOauthCvo.getCode();
		if(Judgment.nullOrBlank(code)){
			bsRvo.setExtend(new WxOauthExtend(false, "获取微信id失败"));
		}else{
			final String openId = WxUtil.oauth(code);
			WxOauthRvo wxOauthRvo = new WxOauthRvo();
			wxOauthRvo.setOpenId(openId);
			
			bsRvo.setBody(wxOauthRvo);
			bsRvo.setExtend(new WxOauthExtend(true, "操作成功"));
		}
		return bsRvo;
	}
	
}
	