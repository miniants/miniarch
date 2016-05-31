package cn.remex.core.cache;
/**
 * @rmx.summary 自定义的数据缓存克隆接口，必须实现Cloneable接口
 * <p>所有自定义的数据缓存实现类要实现克隆方法</p>
 * @author HengYang Liu 
 * @since  2014-6-11
 */
public interface DataCacheCloneable extends java.lang.Cloneable {
	public Object clone() throws CloneNotSupportedException;
}
