package cn.com.qqbx.wx.bs;

import cn.com.qqbx.wx.AiwcConsts;
import cn.com.qqbx.wx.appbean.WxVehicle;
import cn.com.qqbx.wx.appbeans.mmbCenter.MemberExtend;
import cn.com.qqbx.wx.appbeans.wxOauth.WxOauthCvo;
import cn.com.qqbx.wx.appbeans.wxOauth.WxOauthExtend;
import cn.com.qqbx.wx.appbeans.wxOauth.WxOauthRvo;
import cn.com.qqbx.wx.utils.WxUtil;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.util.Judgment;

@BsAnnotation()
public class WxOauthBs implements AiwcConsts {
	
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
	