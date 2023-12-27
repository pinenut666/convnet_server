package net.convnet.server.mybatis.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存:Caffeine
 *
 * @author Lenovo
 */
@Component
public class CaffeineService {

    private Cache<Object, Object> cache;

    public CaffeineService() {
        this.cache = Caffeine.newBuilder()
                //过期策略:一分钟没有读取,则过期删除
                .expireAfterWrite(1, TimeUnit.MINUTES)
                //允许容量100个,超过自动删除
                .maximumSize(100)
                .build();
    }

    /**
     * 存储K-V
     *
     * @param key
     * @param value
     * @return
     */
    public Object put(Object key, Object value) {
        cache.put(key, value);
        return key;
    }

    /**
     * 获取K-V
     *
     * @param key
     * @return
     */
    public Object getIfPresent(Object key) {
        return cache.getIfPresent(key);
    }

    public Object get(Object key, Function<Object, Object> function) {
        return cache.get(key, function);
    }

    public void remove(Object key) {
        cache.invalidate(key);
    }

    public void cleanUp() {
        cache.cleanUp();
    }

    public long estimatedSize() {
        return cache.estimatedSize();
    }

}