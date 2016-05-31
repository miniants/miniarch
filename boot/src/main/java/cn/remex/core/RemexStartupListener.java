package cn.remex.core;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public abstract class RemexStartupListener implements ApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent paramApplicationEvent) {
		if(paramApplicationEvent instanceof RemexStartupEvent){
			onRemexStartupEvent((RemexStartupEvent)paramApplicationEvent);
		}
		
	}
	
	public abstract void onRemexStartupEvent(RemexStartupEvent paramApplicationEvent);
}
