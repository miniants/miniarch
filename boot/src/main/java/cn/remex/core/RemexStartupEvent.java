package cn.remex.core;

import org.springframework.context.ApplicationEvent;

public class RemexStartupEvent extends ApplicationEvent {

	private static final long serialVersionUID = 6552674844345604270L;

	/**
	 * @param source
	 */
	public RemexStartupEvent(Object source) {
		super(source);
	}

	
}
