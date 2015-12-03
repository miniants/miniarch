package cn.com.qqbx.wx;

import anbox.aibc.AiwbUtils;
import cn.remex.bs.Head;
import cn.remex.core.RemexRefreshable;

public class AiwcConfig implements AiwcConsts ,RemexRefreshable{
	public static Head obtainCvoHead(String bs, String oper, String cityCode){
		Head head = new Head();
		head.setTransChannel(TransChannel_web);
		head.setChannelUser("admin");
		head.setChannelPwd("admin");
		head.setChannelOper(oper);
		head.setBs(bs);
		head.setCityCode(cityCode);
//		head.setExecCom();
//		head.setMangCom();
		return head;
	}

	@Override
	public void refresh() {
		AiwbUtils.setSessionMember(null);
//		RSAUtils.generateKeyPair(Rsa_KeyPath);
	}
}
