package com.bookfriend.config;

import com.bookfriend.services.BookService;
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
  public BookService BookService(@Value("${service.class}") String qualifier) {
    return (BookService) context.getBean(qualifier);
  }
}