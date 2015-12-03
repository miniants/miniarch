package cn.com.qqbx.wx.text;

import cn.com.qqbx.wx.model.WxOperaInstruction;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;

public class StoreOper {
	static Container session = ContainerFactory.getSession();
	public static void main(String args[]){
		WxOperaInstruction wxOperaInstructions = new WxOperaInstruction();
		wxOperaInstructions.setInstruction("11");
		wxOperaInstructions.setExecution("cn.com.qqbx.wx.bs.WxComPriceBs");
		session.store(wxOperaInstructions);
	}
}
