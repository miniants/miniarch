package cn.remex.admin.appbeans;

import cn.remex.web.service.BsRvo;

import java.util.List;


public class UtilBsRvo extends BsRvo {
	private List<String> filenames;

	public List<String> getFilenames() {
		return filenames;
	}

	public void setFilenames(List<String> filenames) {
		this.filenames = filenames;
	}
	
}
