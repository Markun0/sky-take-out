package com.sky.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 对数据库内容的缓存， 缓存的是方法的返回值
    public <R, ID> R getCache(String keyPrefix, ID id, Function<ID, R> dbFallback) {
        String key = keyPrefix + id.toString();
        // 从redis中查询缓存
        R r = (R) get(key);
        // 判断是否存在
        if (r!= null) {
            // 存在，直接返回
            return r;
        }
        // 不存在，调用方法，查询数据库
        R apply = dbFallback.apply(id);
        // 不存在，返回错误结果
        if (apply != null) {
            set(key, apply);
            return apply;
        }
        set(key, "");
        return null;
    }

    // 对数据库内容缓存的删除，事务级
    @Transactional(rollbackFor = Exception.class)
    public <ID> void delCache(String keyPrefix, ID id, Consumer<ID> db) {
        String key = keyPrefix + id.toString();
        db.accept(id);
        del(key);
    }

    // 对数据库内容缓存的删除，事务级
    @Transactional(rollbackFor = Exception.class)
    public <ID,R> void delCache(String keyPrefix, ID id,R r, Consumer<R> db) {
        String key = keyPrefix + id.toString();
        db.accept(r);
        del(key);
    }
}
