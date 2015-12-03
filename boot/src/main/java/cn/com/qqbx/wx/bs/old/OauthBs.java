package cn.com.qqbx.wx.bs.old;

import cn.com.qqbx.wx.appbean.ResultBean;
import cn.com.qqbx.wx.appbean.WxVehicle;
import cn.com.qqbx.wx.utils.WxUtil;
import cn.remex.bs.Bs;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;

public class OauthBs implements Bs {
	
	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		WxVehicle wxVehicle = bsCvo.getBody(WxVehicle.class);
		
		/********************网页授权获取用户信息部分****************************/
		
		String code = wxVehicle.getCode();
		final String openId = WxUtil.oauth(code);
		wxVehicle.setOpenId(openId);
		
		
		bsRvo.setBody(wxVehicle);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
		return bsRvo;
	}
	
}
	