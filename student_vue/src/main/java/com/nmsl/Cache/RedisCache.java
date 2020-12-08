package com.nmsl.Cache;

import com.nmsl.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.locks.ReadWriteLock;

@Slf4j
public class RedisCache implements Cache {

    private String id;

    public RedisCache(String id) {
        log.info("当前的缓存id:[{}]",id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {   //放入redis缓存
        log.info("放入缓存的key:[{}] , 放入缓存的value[{}]",key,value);
        getRedisTemplate().opsForHash().put(id,key.toString(),value);
    }

    @Override
    public Object getObject(Object key) {   //从redis缓存获取
        log.info("获取缓存的key信息:[{}]",key.toString());
        return getRedisTemplate().opsForHash().get(id,key.toString());
    }

    @Override
    public Object removeObject(Object key) {    //删除指定缓存信息
        return null;
    }

    @Override
    public void clear() {   //清除缓存
        log.info("清除所有缓存...");
        getRedisTemplate().delete(id);
    }

    @Override
    public int getSize() {
        return getRedisTemplate().opsForHash().size(id).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    //封装获取redisTemplate
    public RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        //redistemplate都是对象序列化,key想要直接使用字符串序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
