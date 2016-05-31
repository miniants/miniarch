package cn.remex.redis;

import cn.remex.RemexConstants;
import cn.remex.core.RemexApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * Created by guoqi on 2016/3/3.
 */
@Repository(value = "redisDataSource")
public class RedisDataSource {
    private ShardedJedisPool shardedJedisPool = (ShardedJedisPool) RemexApplication.getContext().getBean("shardedJedisPool");
    public ShardedJedis getRedisClient() {
        try {
            return shardedJedisPool.getResource();
        } catch (Exception e) {
            RemexConstants.logger.info("getRedisClent error", e);
        }
        return null;
    }

}
