package zbh.wx.bs.old;

import zbh.wx.appbean.ResultBean;
import zbh.wx.appbean.WxVehicle;
import zbh.wx.utils.WxUtil;
import zbh.remex.bs.Bs;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;

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
	