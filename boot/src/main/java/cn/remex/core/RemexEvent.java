package cn.remex.core;

import org.springframework.context.ApplicationEvent;

public class RemexEvent extends ApplicationEvent {
	private Object[] params;

	public RemexEvent(String source, Object... params) {
		super(source);
		this.params = params;
	}


	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
}
