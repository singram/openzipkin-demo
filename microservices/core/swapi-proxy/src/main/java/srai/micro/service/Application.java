package srai.micro.service;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import srai.micro.service.model.CachableSwapiCharacter;

@ComponentScan({"srai.common.micro.service.util", "srai.micro.service"})
@SpringBootApplication
@EnableFeignClients
@RibbonClient(name = "swapi-service", configuration = RibbonConfigSwapiService.class)
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Bean
  public AlwaysSampler defaultSampler() {
    return new AlwaysSampler();
  }

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

class RibbonConfigSwapiService {
  @Bean
  ServerList<Server> ribbonServerList() {
    return new StaticServerList<>(new Server("swapi.co", 80));
  }
}
