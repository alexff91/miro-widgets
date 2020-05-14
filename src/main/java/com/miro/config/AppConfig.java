package com.miro.config;

import com.miro.services.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public WidgetService WidgetService(@Value("${service.class}") String qualifier) {
        return (WidgetService) context.getBean(qualifier);
    }
}