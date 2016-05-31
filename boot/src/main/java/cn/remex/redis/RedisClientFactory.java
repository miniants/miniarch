package cn.remex.redis;

import cn.remex.RemexConstants;
import cn.remex.core.RemexApplication;
import org.apache.log4j.Logger;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by guoqi on 2016/3/3.
 */
public class RedisClientFactory {

    Logger log = RemexConstants.logger;
    private RedisDataSource redisDataSource =  RemexApplication.getBean(RedisDataSource.class);

    public RedisDataSource getRedisDataSource() {
        return redisDataSource;
    }

    public void setRedisDataSource(RedisDataSource redisDataSource) {
        this.redisDataSource = redisDataSource;
    }

    public void disconnect() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        shardedJedis.disconnect();
    }

    /**
     * 设置单个值
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }
    /**
     * 设置单个值
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value,String nxxx,String expx,long time) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.set(key, value,nxxx,expx,time);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }
    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     *  判断key否存在
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return false;
        }
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 查看key所储存的值的类型
     * @param key
     * @return
     */
    public String type(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.type(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 设置一个key的过期时间（单位：秒）
     *
     * @param key
     *            key值
     * @param seconds
     *            多少秒后过期
     * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
     */
    public Long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }
    /**
     * 设置一个key在某个时间点过期
     *
     * @param key
     *            key值
     * @param unixTime
     *            unix时间戳，从1970-01-01 00:00:00开始到现在的秒数
     * @return 1：设置了过期时间 0：没有设置过期时间/不能设置过期时间
     */
    public Long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.expireAt(key, unixTime);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     *   查看某个key的剩余生存时间,单位【秒】.永久生存或者不存在的都返回-1
     * @param key
     * @return
     */
    public Long ttl(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        try {
            result = shardedJedis.ttl(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 获取原值，更新为新值一步完成
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 新增键值对时防止覆盖原先值
     * 原先key302不存在时，新增key302
     * 当key302存在时，尝试新增key302
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        try {
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 超过有效期键值对被删除
     * 设置key的有效期，并存储数据
     * 新增key303，并指定过期时间为2秒
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String setex(String key, int seconds, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.setex(key, seconds, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 名称为key的string的值附加value
     * @param key
     * @param value
     * @return
     */
    public Long append(String key, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.append(key, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     *
     * 返回名称为key的string的value的子串
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String substr(String key, int start, int end) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.substr(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 设置HashSet对象
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.hset(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 设置HashSet对象
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key, Map<String,String> map) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return false;
        }
        try {
            result = shardedJedis.hmset(key, map);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result=="OK";
    }

    /**
     *  返回名称为key的hash中field对应的value
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.hget(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 删除一个key
     * @param key
     * @return
     */
    public Long del(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.del(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }


    /**
     * 删除名称为key的hash中键为field的域
     * @param key
     * @param field
     * @return
     */
    public Long hdel(String key, String field) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.hdel(key, field);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }



    /**
     * 返回 domain 指定的哈希集中所有字段的value值
     * @param key
     * @return
     */
    public List<String> hvals(String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.hvals(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     *返回名称为key的hash中所有的键（field）及其对应的value
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.hgetAll(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }
    /**
     * 在名称为key的list尾添加一个值为value的元素
     * @param key
     * @param string
     * @return
     */
    public Long rpush(String key, String string) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.rpush(key, string);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     *在名称为key的list头添加一个值为value的 元素
     * @param key
     * @param string
     * @return
     */
    public Long lpush(String key, String string) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.lpush(key, string);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 返回名称为key的list的长度
     * @param key
     * @return
     */
    public Long lLength(String key) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.llen(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 返回名称为key的list中start至end之间的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> listRange(String key, long start, long end) {
        List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.lrange(key, start, end);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 截取名称为key的list
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String listTrim(String key, long start, long end) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.ltrim(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 返回名称为key的list中index位置的元素
     * @param key
     * @param index
     * @return
     */
    public String listIndex(String key, long index) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.lindex(key, index);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 给名称为key的list中index位置的元素赋值
     * @param key
     * @param index
     * @param value
     * @return
     */
    public String listSet(String key, long index, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.lset(key, index, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 删除count个key的list中值为value的元素
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lrem(String key, long count, String value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        try {
            result = shardedJedis.lrem(key, count, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 返回并删除名称为key的list中的首元素
     * @param key
     * @return
     */
    public String lpop(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.lpop(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 返回并删除名称为key的list中的尾元素
     * @param key
     * @return
     */
    public String rpop(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.rpop(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 向名称为key的set中添加元素member
     * @param key
     * @param member
     * @return
     */
    public Long sadd(String key, String... member) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.sadd(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     * 返回名称为key的set的所有元素
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        try {
            result = shardedJedis.smembers(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }

    /**
     *删除名称为key的set中的元素member
     * @param key
     * @param member
     * @return
     */
    public Long srem(String key, String member) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Long result = null;
        if (shardedJedis == null) {
            return null;
        }
        try {
            result = shardedJedis.srem(key, member);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }
//
//    public String spop(String key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        String result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.spop(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long scard(String key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        Long result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.scard(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Boolean sismember(String key, String member) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        Boolean result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.sismember(key, member);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String srandmember(String key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        String result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.srandmember(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zadd(String key, double score, String member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.zadd(key, score, member);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<String> zrange(String key, int start, int end) {
//        Set<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.zrange(key, start, end);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zrem(String key, String member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.zrem(key, member);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Double zincrby(String key, double score, String member) {
//        Double result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zincrby(key, score, member);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zrank(String key, String member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrank(key, member);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zrevrank(String key, String member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrank(key, member);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<String> zrevrange(String key, int start, int end) {
//        Set<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrange(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeWithScores(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeWithScores(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zcard(String key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zcard(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Double zscore(String key, String member) {
//        Double result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zscore(key, member);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<String> sort(String key) {
//        List<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.sort(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<String> sort(String key, SortingParams sortingParameters) {
//        List<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.sort(key, sortingParameters);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zcount(String key, double min, double max) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zcount(key, min, max);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<String> zrangeByScore(String key, double min, double max) {
//        Set<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScore(key, min, max);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<String> zrevrangeByScore(String key, double max, double min) {
//        Set<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScore(key, max, min);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
//        Set<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScore(key, min, max, offset, count);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
//        Set<String> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScoreWithScores(key, min, max);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zremrangeByRank(String key, int start, int end) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zremrangeByRank(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zremrangeByScore(String key, double start, double end) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zremrangeByScore(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.linsert(key, where, pivot, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String set(byte[] key, byte[] value) {
//        String result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.set(key, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] get(byte[] key) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.get(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Boolean exists(byte[] key) {
//        Boolean result = false;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.exists(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String type(byte[] key) {
//        String result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.type(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long expire(byte[] key, int seconds) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.expire(key, seconds);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long expireAt(byte[] key, long unixTime) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.expireAt(key, unixTime);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long ttl(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.ttl(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] getSet(byte[] key, byte[] value) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.getSet(key, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long setnx(byte[] key, byte[] value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.setnx(key, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String setex(byte[] key, int seconds, byte[] value) {
//        String result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.setex(key, seconds, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long decrBy(byte[] key, long integer) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.decrBy(key, integer);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long decr(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.decr(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long incrBy(byte[] key, long integer) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.incrBy(key, integer);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long incr(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.incr(key);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long append(byte[] key, byte[] value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.append(key, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] substr(byte[] key, int start, int end) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.substr(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long hset(byte[] key, byte[] field, byte[] value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hset(key, field, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] hget(byte[] key, byte[] field) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hget(key, field);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hsetnx(key, field, value);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
//        String result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hmset(key, hash);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<byte[]> hmget(byte[] key, byte[]... fields) {
//        List<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hmget(key, fields);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        try {

            result = shardedJedis.hincrBy(key, field, value);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            shardedJedis.close();
        }
        return result;
    }
//
//    public Boolean hexists(byte[] key, byte[] field) {
//        Boolean result = false;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hexists(key, field);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long hdel(byte[] key, byte[] field) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hdel(key, field);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long hlen(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hlen(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> hkeys(byte[] key) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hkeys(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Collection<byte[]> hvals(byte[] key) {
//        Collection<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hvals(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Map<byte[], byte[]> hgetAll(byte[] key) {
//        Map<byte[], byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.hgetAll(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long rpush(byte[] key, byte[] string) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.rpush(key, string);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long lpush(byte[] key, byte[] string) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.lpush(key, string);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long llen(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.llen(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<byte[]> lrange(byte[] key, int start, int end) {
//        List<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.lrange(key, start, end);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String ltrim(byte[] key, int start, int end) {
//        String result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.ltrim(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] lindex(byte[] key, int index) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.lindex(key, index);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String lset(byte[] key, int index, byte[] value) {
//        String result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.lset(key, index, value);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long lrem(byte[] key, int count, byte[] value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.lrem(key, count, value);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] lpop(byte[] key) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.lpop(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] rpop(byte[] key) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.rpop(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long sadd(byte[] key, byte[] member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.sadd(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> smembers(byte[] key) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.smembers(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long srem(byte[] key, byte[] member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.srem(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] spop(byte[] key) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.spop(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long scard(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.scard(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Boolean sismember(byte[] key, byte[] member) {
//        Boolean result = false;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.sismember(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public byte[] srandmember(byte[] key) {
//        byte[] result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.srandmember(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zadd(byte[] key, double score, byte[] member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zadd(key, score, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> zrange(byte[] key, int start, int end) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrange(key, start, end);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zrem(byte[] key, byte[] member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrem(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Double zincrby(byte[] key, double score, byte[] member) {
//        Double result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zincrby(key, score, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zrank(byte[] key, byte[] member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrank(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zrevrank(byte[] key, byte[] member) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrank(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> zrevrange(byte[] key, int start, int end) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrange(key, start, end);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeWithScores(key, start, end);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeWithScores(key, start, end);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zcard(byte[] key) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zcard(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Double zscore(byte[] key, byte[] member) {
//        Double result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zscore(key, member);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<byte[]> sort(byte[] key) {
//        List<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.sort(key);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
//        List<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.sort(key, sortingParameters);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zcount(byte[] key, double min, double max) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zcount(key, min, max);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScore(key, min, max);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScore(key, min, max, offset, count);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScoreWithScores(key, min, max);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScore(key, max, min);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
//        Set<byte[]> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
//        Set<Tuple> result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zremrangeByRank(byte[] key, int start, int end) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zremrangeByRank(key, start, end);
//
//        } catch (Exception e) {
//
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long zremrangeByScore(byte[] key, double start, double end) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.zremrangeByScore(key, start, end);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
//        Long result = null;
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//
//            result = shardedJedis.linsert(key, where, pivot, value);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        List<Object> result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.pipelined(shardedJedisPipeline);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Jedis getShard(byte[] key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        Jedis result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getShard(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Jedis getShard(String key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        Jedis result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getShard(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public JedisShardInfo getShardInfo(byte[] key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        JedisShardInfo result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getShardInfo(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public JedisShardInfo getShardInfo(String key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        JedisShardInfo result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getShardInfo(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public String getKeyTag(String key) {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        String result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getKeyTag(key);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Collection<JedisShardInfo> getAllShardInfo() {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        Collection<JedisShardInfo> result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getAllShardInfo();
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }
//
//    public Collection<Jedis> getAllShards() {
//        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
//        Collection<Jedis> result = null;
//        if (shardedJedis == null) {
//            return result;
//        }
//        boolean broken = false;
//        try {
//            result = shardedJedis.getAllShards();
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            broken = true;
//        } finally {
//            redisDataSource.returnResource(shardedJedis, broken);
//        }
//        return result;
//    }

}
