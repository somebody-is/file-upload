package com.lzf.fileupload.config;


import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 1697712297
 * @date 2022/3/20
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 默认到期时间180s。
     */
    private Duration timeToLive = Duration.ofSeconds(180);

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Autowired
    public RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String,Object> redisTemplate() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate().getValueSerializer()));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);

    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (Object obj, Method method, Object[] args) -> {
            StringBuilder stringBuilder = new StringBuilder();
            //stringBuilder.append(obj.getClass().getSimpleName());
            //stringBuilder.append(".");
            stringBuilder.append(method.getName());
            stringBuilder.append("[");
      /*for (Object arg : args) {
        stringBuilder.append(arg.toString());
        stringBuilder.append(",");
      }*/
            String strArgs = Stream.of(args).map(Object::toString).collect(Collectors.joining(","));
            stringBuilder.append(strArgs);
            stringBuilder.append("]");
            log.debug("keyGenerator:{}", stringBuilder.toString());
            return stringBuilder.toString();
        };
    }
}
