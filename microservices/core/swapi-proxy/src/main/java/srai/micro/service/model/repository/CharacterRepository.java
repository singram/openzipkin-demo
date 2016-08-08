package srai.micro.service.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import srai.micro.service.model.CachableSwapiCharacter;

@Service
public class CharacterRepository implements Repository<CachableSwapiCharacter> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CharacterRepository.class);

  @Value("${spring.redis.host:localhost}")
  private String redisHostName;

  @Value("${spring.redis.port:6379}")
  private String redisPort;

  private String redisURI;

  @Autowired
  RedisTemplate<String, CachableSwapiCharacter> redisTemplate;

  @Autowired
  Tracer tracer;

  private String getRedisURI() {
    if (this.redisURI == null) {
      redisURI = redisHostName + ":" + redisPort;
    }
    return redisURI;
  }

  @Override
  public void put(CachableSwapiCharacter character) {
    Span newSpan = this.tracer.createSpan("redis");
    try {
      newSpan.tag("redis.op", "put");
      newSpan.tag("lc", "redis");
      newSpan.tag("redis.hostname", getRedisURI());
      newSpan.logEvent(Span.CLIENT_SEND);
      redisTemplate.opsForHash().put(character.getObjectKey(), character.getKey(), character);
    } finally {
      newSpan.tag("peer.service", "redis");
      newSpan.tag("peer.ipv4", redisHostName);
      newSpan.tag("peer.port", redisPort);
      //newSpan.tag("sa", getRedisURI());
      newSpan.logEvent(Span.CLIENT_RECV);
      this.tracer.close(newSpan);
    }
  }

  @Override
  public void delete(CachableSwapiCharacter key) {
    Span newSpan = this.tracer.createSpan("redis");
    try {
      newSpan.tag("redis.op", "delete");
      newSpan.tag("lc", "redis");
      newSpan.tag("redis.hostname", getRedisURI());
      newSpan.logEvent(Span.CLIENT_SEND);
      redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
    } finally {
      newSpan.tag("peer.service", "redis");
      newSpan.tag("peer.ipv4", redisHostName);
      newSpan.tag("peer.port", redisPort);
      //newSpan.tag("sa", getRedisURI());
      newSpan.logEvent(Span.CLIENT_RECV);
      this.tracer.close(newSpan);
    }
  }

  @Override
  public CachableSwapiCharacter get(CachableSwapiCharacter key) {
    Span newSpan = this.tracer.createSpan("redis");
    try {
      newSpan.tag("redis.op", "get");
      newSpan.tag("lc", "redis");
      newSpan.tag("redis.hostname", getRedisURI());
      newSpan.logEvent(Span.CLIENT_SEND);
      return (CachableSwapiCharacter) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
    } finally {
      newSpan.tag("peer.service", "redis");
      newSpan.tag("peer.ipv4", redisHostName);
      newSpan.tag("peer.port", redisPort);
      //newSpan.tag("sa", getRedisURI());
      newSpan.logEvent(Span.CLIENT_RECV);
      this.tracer.close(newSpan);
    }
  }
}
