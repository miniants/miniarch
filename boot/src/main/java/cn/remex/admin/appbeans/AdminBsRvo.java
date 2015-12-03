package cn.remex.admin.appbeans;

import cn.remex.db.model.SysMenu;
import cn.remex.web.service.BsRvo;

import java.util.List;


public class AdminBsRvo extends BsRvo {
	
	private SysMenu sysMenu;
	private List<?> datas;
	private DataMeta dataMeta;

	public SysMenu getSysMenu() {
		return sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	public List<?> getDatas() {
		return datas;
	}

	public void setDatas(List<?> datas) {
		this.datas = datas;
	}

	public DataMeta getDataMeta() {
		return dataMeta;
	}

	public void setDataMeta(DataMeta dataMeta) {
		this.dataMeta = dataMeta;
	}

	
	
	
}
