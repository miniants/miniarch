package cn.com.qqbx.wx.bs;

import net.sf.json.JSONObject;
import cn.com.qqbx.wx.AiwcConsts;
import cn.com.qqbx.wx.utils.WxUtil;

public class WxObtainIpBs implements AiwcConsts {
	public static void main(String args[]){
		JSONObject jsonObject = WxUtil.obtainIpOfWX();
		System.out.println(jsonObject);
	}
}
	