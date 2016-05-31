package cn.remex.web.service.appbeans;

import cn.remex.web.service.BsCvo;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/5/24 0024.
 */
public class AsyncCvo extends BsCvo {
	private String asyncKey;
	private double asyncProgress;
	private String asyncMsg;
	private boolean asyncRestart;

	public String getAsyncKey() {
		return asyncKey;
	}

	public boolean getAsyncRestart() {
		return asyncRestart;
	}

	public void setAsyncRestart(boolean asyncRestart) {
		this.asyncRestart = asyncRestart;
	}

	public void setAsyncKey(String asyncKey) {
		this.asyncKey = asyncKey;
	}

	public String getAsyncMsg() {
		return asyncMsg;
	}

	public void setAsyncMsg(String asyncMsg) {
		this.asyncMsg = asyncMsg;
	}

	public double getAsyncProgress() {
		return asyncProgress;
	}

	public void setAsyncProgress(double asyncProgress) {
		this.asyncProgress = asyncProgress;
	}
}
