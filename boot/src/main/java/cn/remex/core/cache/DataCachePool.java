
package cn.remex.core.cache;

import cn.remex.RemexConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
/**
 * @rmx.summary 数据缓存池,是一个泛型类，泛型类型必须实现DataCacheCloneable接口
 * @author HengYang Liu
 * @since  2014-6-11
 */
public class DataCachePool<T extends DataCacheCloneable> implements RemexConstants {

	private static Map<String, Map<Object, Object>> caches = new HashMap<String, Map<Object, Object>>();
	/**
	 * 根据缓存类型和名称获取对应的缓存对象
	 * @rmx.summary 根据缓存类型和名称获取对应的缓存对象
	 * @param type 缓存类型
	 * @param key 缓存关键字，如缓存名称
	 * @return Object 缓存对象
	 * @rmx.call {@link link cn.remex.db.rsql.RsqlUtils}
	 */
	public static Object get(String type,Object key){
		Map<Object, Object> cache = caches.get(type);
		return null==cache?null:caches.get(type).get(key);
	}
	/**
	 * 将一个对象放置指定类型和指定名称的缓存区中
	 * @rmx.summary 将一个对象放置指定类型和指定名称的缓存区中
	 * @param type 缓存池类型
	 * @param key 缓存关键字，用来标志一个对象
	 * @param value 缓存对象
	 * @rmx.call {@link cn.remex.db.rsql.RsqlUtils}
	 */
	public static void put(String type,Object key,Object value){
		Map<Object, Object> cache = caches.get(type);
		if(null == cache){
			cache = new HashMap<Object, Object>();
			caches.put(type, cache);
		}
		cache.put(key, value);
	}
	/**
	 * 清空缓存区域
	 * @rmx.summary 清空缓存区域
	 * @rmx.call {@link cn.remex.core.RemexApplication}
	 */
	public static void reset(){
		caches = new HashMap<String, Map<Object,Object>>();
	}
	/**
	 * 创建数据缓存池，该方法是线程安全的
	 * @rmx.summary 创建数据缓存池，如果当前缓存池中没有指定名称的数据缓存池，创建一个新
	 * <p>的数据缓存池，并将对象添加至缓存池，该方法是线程安全的</p>
	 * @param t 放入缓存池中的对象实例，该实例类必须实现DataCacheCloneable
	 * @param k 数据缓存池名称
	 * @param pools 缓存池集合 其中key:数据缓存池的名称，value：缓存池
	 * @return  <PT extends DataCacheCloneable> 缓存池
	 * @rmx.call {@link cn.remex.db.rsql.sql.SqlBean}
	 */
	public synchronized static <PT extends DataCacheCloneable> DataCachePool<PT> createPool(final PT t/*, final String k,
			final Map<String, DataCachePool<PT>> pools*/) {
//		DataCachePool<PT> pool = pools.get(k);
//		if (null == pool) {
//			pool = new DataCachePool<PT>(t);
//			pools.put(k, pool);
//			logger.info("没有名" + k + " 的DataCachePool,系统已成功尝试自动创建一个并放入缓存池中！");
//		}
//		return pool;
		return new DataCachePool<PT>(t);
	}

	/**
	 * 用于保存当前产生的个数。
	 */
	private int curCount = 1;

	/**
	 * 用于保存每个缓冲池最大的缓冲个数。必须大于curCount和1
	 */
	private int maxCount = 16;
	/**
	 * 构造当前Cvo池，
	 */
	private Vector<T> vector = new Vector<T>();

	@SuppressWarnings("unchecked")
	private DataCachePool(final T t) {
		// 构造当前Cvo池，默认情况构造三个对象
		this.vector.addElement(t);
		try {
			this.vector.addElement((T) t.clone());
			this.curCount++;
			this.vector.addElement((T) t.clone());
			this.curCount++;
		} catch (CloneNotSupportedException e) {
			throw new PoolNotSupportedBeansException(t.getClass()) ;
		}
	}
    /**
     * 为当前Cvo缓存池增加缓存缓存对象，当缓存数量超过最大值时，当前总数减1，无法添加新的对象到缓存池
     * @rmx.summary 为当前Cvo缓存池增加缓存缓存对象，当缓存数量超过最大值时，当前总数减1，无法添加新的对象到缓存池
     * @param t 缓存对象
     * @rmx.call {@link cn.remex.db.rsql.sql.SqlBean}
     */
	public synchronized void add(final T t) {
		if(loggerDebug)logger.debug(t.getClass()+"回收中,当前总数:"+curCount+";"+t.toString());
		if (this.vector.size() >= this.maxCount)
			this.curCount--;
		else this.vector.add(t);
	}
    /**
     *获取缓存池中的元素 
     *@rmx.summary 获取缓存池中的元素 ，缓存池存放对象大于1时，直接获取第一个，则缓存池对应少一个；小于1时，
     *<p>返回当前对象的一个副本</p>
     *@return T 缓存元素
     *@rmx.call {@link cn.remex.db.rsql.sql.SqlBean}
     */
	@SuppressWarnings("unchecked")
	public synchronized T get() {
		T t = null;
		if (this.vector.size() > 1) {
			t = this.vector.firstElement();
			this.vector.removeElementAt(0);
		} else
			try {
				t = (T) this.vector.firstElement().clone();
				this.curCount++;
			} catch (CloneNotSupportedException e) {
				// 如果无法生成备份，则返回null
				t = null;
			}
		//	else {
		//			// 所有缓存对象正在使用，等待，在add()处notify
		//			synchronized (this) {
		//				try {
		//					this.waitCount++;
		//					logger.debug(this.vector.firstElement().getClass()+"的缓冲等待中,当前等待:"+waitCount);
		//					wait();
		//					t = get();
		//					this.waitCount--;
		//				} catch (InterruptedException e) {
		//					t = null;
		//					e.printStackTrace();
		//				}
		//			}
		//		}
		return t;
	}
    /**
     * 获取缓存区的大小
     * @rmx.summary 获取缓存区的大小
     * @return int 缓存区大小
     */
	public int size() {
		return this.vector.size();
	}
}
