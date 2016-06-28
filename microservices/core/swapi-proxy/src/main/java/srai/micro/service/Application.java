package srai.micro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import srai.micro.service.model.CachableSwapiCharacter;

@ComponentScan({"srai.common.micro.service.util", "srai.micro.service"})
@SpringBootApplication
@EnableFeignClients
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  //  @Bean
  //  public AlwaysSampler defaultSampler() {
  //    return new AlwaysSampler();
  //  }

  @Bean
  RedisTemplate<String, CachableSwapiCharacter> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, CachableSwapiCharacter> template = new RedisTemplate<String, CachableSwapiCharacter>();
    template.setConnectionFactory(connectionFactory);
    return template;
  }


  /** Main entry point. */
  public static void main(final String... args) {
    SpringApplication.run(Application.class, args);
  }

}
