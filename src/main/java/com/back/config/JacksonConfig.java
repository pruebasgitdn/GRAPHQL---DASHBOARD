package com.back.config;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {


    @Bean
    public ObjectMapper objectMapper() {

        //serializer con config de fechas
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

//        mapper.activateDefaultTyping(
//                BasicPolymorphicTypeValidator.builder()
//                        .allowIfSubType(Object.class)
//                        .build(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );

        return mapper;
    }







}
