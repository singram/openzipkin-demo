package srai.micro.service;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.net.URI;
import java.net.URISyntaxException;

@ComponentScan({"srai.common.micro.service.util", "srai.micro.service"})
@SpringBootApplication
@EnableFeignClients
public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  @Bean
  public AlwaysSampler defaultSampler() {
    return new AlwaysSampler();
  }

  /** Main entry point. */
  public static void main(final String... args) {
    SpringApplication.run(Application.class, args);
  }

}

@RibbonClient(name = "quote-service", configuration = RibbonConfigQuoteService.class)
class RibbonConfigQuoteService {

  //@Value("${srai.micro.service.quote-service.url:'http://quote-service:4567'}")
  @Value("${srai.micro.service.quote-service.url}")
  private String quoteServerURL;

  static private String DEFAULT_QUOTE_SERVER_HOST = "quote-service";
  static private int    DEFAULT_QUOTE_SERVER_PORT = 4567;

  @Bean
  ServerList<Server> ribbonServerList() {
    String hostname;
    int port;
    try {
      hostname = new URI(quoteServerURL).getHost();
      port = new URI(quoteServerURL).getPort();
    } catch (URISyntaxException e) {
      hostname = DEFAULT_QUOTE_SERVER_HOST;
      port = DEFAULT_QUOTE_SERVER_PORT;
      e.printStackTrace();
    }
    return new StaticServerList<>(new Server(hostname, port));
  }
}


@RibbonClient(name = "swapi-service", configuration = RibbonConfigSwapiService.class)
class RibbonConfigSwapiService {
  @Bean
  ServerList<Server> ribbonServerList() {
    return new StaticServerList<>(new Server("swapi-proxy", 8080));
  }
}