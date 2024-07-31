package com.beyond.festival.common.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    public String host; //localhost

    @Value("${spring.redis.port}")
    public int port; //6379

    @Bean
    @Qualifier("1")
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(1);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("1")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("1") RedisConnectionFactory redisConnectionFactory){
        // Object : 보통 json 형태의 데이터가 들어올 것
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key : String으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value : json으로 직렬화
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("2")
    public RedisConnectionFactory redisStockFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(2);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("2")
    public RedisTemplate<String, Object> redisStockTemplate(@Qualifier("2") RedisConnectionFactory redisStockFactory){
        // Object : 보통 json 형태의 데이터가 들어올 것
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key : String으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value : json으로 직렬화
        redisTemplate.setConnectionFactory(redisStockFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("3")
    public RedisConnectionFactory redisLikeFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(3);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("3")
    public RedisTemplate<String, Object> redisLikeTemplate(@Qualifier("3") RedisConnectionFactory redisLikeFactory){
        // Object : 보통 json 형태의 데이터가 들어올 것
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key : String으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value : json으로 직렬화
        redisTemplate.setConnectionFactory(redisLikeFactory);
        return redisTemplate;
    }


}
