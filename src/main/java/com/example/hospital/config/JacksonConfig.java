package com.example.hospital.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson配置类，用于处理Hibernate懒加载序列化问题和Java 8时间类型
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 添加Java 8时间模块支持
        objectMapper.registerModule(new JavaTimeModule());
        // 禁用将日期写为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // 添加Hibernate6模块来处理懒加载
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        // 强制懒加载代理的序列化
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        // 将未初始化的代理写为null
        hibernate6Module.configure(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL, true);
        // 忽略标识符字段
        hibernate6Module.configure(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION, false);
        
        objectMapper.registerModule(hibernate6Module);
        
        // 禁用序列化空Bean时抛出异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        return objectMapper;
    }
}
