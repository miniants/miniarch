package zbh.wx.bs;

import net.sf.json.JSONObject;
import zbh.wx.AiwcConsts;
import zbh.wx.utils.WxUtil;

public class WxObtainIpBs implements AiwcConsts {
	public static void main(String args[]){
		JSONObject jsonObject = WxUtil.obtainIpOfWX();
		System.out.println(jsonObject);
	}
}
	