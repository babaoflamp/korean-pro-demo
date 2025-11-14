package com.mk.config.webmvc;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mk.config.interceptor.WebLogInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomWebMvcConfig implements WebMvcConfigurer {

  private final WebLogInterceptor webLogInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
      .addInterceptor(webLogInterceptor)
      .excludePathPatterns( "/",
                            "/pub/css/**",
                            "/pub/img/**",
                            "/pub/js/**",
                            "/js/**");
  }

  // ObjectMapper module
  @Bean
  public ObjectMapper objectMapper() {
    return Jackson2ObjectMapperBuilder
        .json()
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .modules(new JavaTimeModule())
        .build();
  }

  // session을 사용한 message 설정
  @Bean
  public SessionLocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(new Locale("ko"));

    return sessionLocaleResolver;
  }

}
