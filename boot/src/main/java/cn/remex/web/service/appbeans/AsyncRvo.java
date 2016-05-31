package cn.remex.web.service.appbeans;

import cn.remex.db.model.SysStatus;
import cn.remex.db.rsql.RsqlConstants.SysStatusEnum;
import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/5/24 0024.
 */
public class AsyncRvo extends BsRvo {
	private String asyncKey;
	private double asyncProgress;
	private String asyncMsg;

	private SysStatus asyncStatus;

	private Function<AsyncRvo,AsyncRvo> startFunction;
	private Function<AsyncRvo,AsyncRvo> processFunction = asyncRvo-> asyncRvo;
	private Function<AsyncRvo,AsyncRvo> successFunction = asyncRvo-> asyncRvo;
	private Consumer<AsyncRvo> _updateConsumer = asyncRvo -> {};

	public AsyncRvo start(Function<AsyncRvo,AsyncRvo> consumer){
		startFunction = consumer;
		return this;
	}
	public AsyncRvo process(Function<AsyncRvo,AsyncRvo> consumer){
		processFunction = consumer;
		return this;
	}
	public AsyncRvo success(Function<AsyncRvo,AsyncRvo> consumer){
		successFunction = consumer;
		return this;
	}
	public AsyncRvo _update(Consumer<AsyncRvo> consumer){
		_updateConsumer = consumer;
		return this;
	}

	public AsyncRvo update(SysStatusEnum status, double progressRate, String desc) {
		asyncStatus.setStatus(status);
		asyncStatus.setProgressRate(progressRate);
		asyncStatus.setDesc(desc);
		_updateConsumer.accept(this);
		return this;
	}


	public Function<AsyncRvo,AsyncRvo> getProcessFunction() {
		return processFunction;
	}

	public Function<AsyncRvo, AsyncRvo> getStartFunction() {
		return startFunction;
	}

	public Function<AsyncRvo,AsyncRvo> getSuccessFunction() {
		return successFunction;
	}

	public SysStatus getAsyncStatus() {
		return asyncStatus;
	}

	public void setAsyncStatus(SysStatus asyncStatus) {
		this.asyncStatus = asyncStatus;
	}

	public String getAsyncKey() {
		return asyncKey;
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
