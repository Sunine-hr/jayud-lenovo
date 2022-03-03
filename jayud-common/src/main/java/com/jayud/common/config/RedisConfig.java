package com.jayud.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * @anthor Satellite
 * ShiroConfig
 * Shiro 配置
 * http://www.javalow.com
 * @date 2018-12-08-15:59
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConnectionFactory factory;

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jacksonSerializer();
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    @ConditionalOnMissingBean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    @ConditionalOnMissingBean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    @ConditionalOnMissingBean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    @ConditionalOnMissingBean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    private Jackson2JsonRedisSerializer jacksonSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }


    /**
     * 配置自动化缓存使用的序列化方式以及过期时间
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .entryTtl(Duration.ofDays(1));
        return configuration;
    }

    /**
     * 缓存过期时间自定义配置
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 设置CacheManager的值序列化方式为Jackson2JsonRedisSerializer,默认就是使用StringRedisSerializer序列化key,JdkSerializationRedisSerializer序列化value
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // 配置value序列化方式为Jackson2JsonRedisSerializer,key序列化方式采用默认的StringRedisSerializer
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)).entryTtl(Duration.ofMinutes(30));
        // 每一类信息进行缓存配置
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
//        // 用户信息
//        redisCacheConfigurationMap.put(RedisConstant.USER, cacheConfiguration.entryTtl(Duration.ofDays(10))
//                .disableCachingNullValues().prefixKeysWith(RedisConstant.USER));

        // 初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        // 设置默认超过期时间是30min(短时间的已经做了其他的处理,不会采用注解形式加入缓存)
        defaultCacheConfig.entryTtl(Duration.ofMinutes(30));
        // 初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration,
                redisCacheConfigurationMap);
        return cacheManager;
    }
//
//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//        //设置缓存过期时间
//        Map<String, Long> expires = new HashMap<>();
//        expires.put("12h", 3600 * 12L);
//        rcm.setExpires(expires);
////        rcm.setDefaultExpiration(60 * 60 * 12);//默认过期时间
//        return rcm;
//    }




}
