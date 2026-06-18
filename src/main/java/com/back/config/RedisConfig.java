package com.back.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;


//Config para meter el bean
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;


    //Este bean me coge lo que manden los serviciosImpl
        //Con @Cacheable
        @Bean
        public RedisCacheManager cacheManager(
                RedisConnectionFactory factory
        ) {

            ObjectMapper redisMapper = objectMapper.copy();


            //serializer con config de fechas
            //ObjectMapper mapper = new ObjectMapper();
            redisMapper.registerModule(new JavaTimeModule());
            redisMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            redisMapper.activateDefaultTyping(
                    BasicPolymorphicTypeValidator.builder()
                            .allowIfSubType(Object.class)
                            .build(),
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
            );

            GenericJackson2JsonRedisSerializer serializer =
                    new GenericJackson2JsonRedisSerializer(redisMapper);


            //Duracion del item en cache
            RedisCacheConfiguration config =
                    RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofMinutes(5))
                            .serializeValuesWith(
                                    RedisSerializationContext.SerializationPair
                                            .fromSerializer( //Serializar un JSON a redis
                                                    serializer
                                                    //new GenericJackson2JsonRedisSerializer()

                                            )
                            )
                    ;

            return RedisCacheManager.builder(factory)
                    .cacheDefaults(config)
                    .build();
        }
}