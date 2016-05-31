package cn.remex.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/5/25 0025.
 */
public class ThreadHelper {
	private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	public static void run(Runnable target){
		cachedThreadPool.execute(target);
	}
}
