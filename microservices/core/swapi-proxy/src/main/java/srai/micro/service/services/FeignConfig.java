package srai.micro.service.services;

import feign.Logger;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }


  @Bean
  public RibbonCommandFactory<?> ribbonCommandFactory(SpringClientFactory clientFactory) {
    return new HttpClientRibbonCommandFactory(clientFactory);
  }
}
