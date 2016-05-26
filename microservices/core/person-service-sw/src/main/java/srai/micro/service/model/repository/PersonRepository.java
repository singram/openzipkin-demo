package srai.micro.service.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import srai.micro.service.model.CachableSwapiPerson;

@Service
public class PersonRepository implements Repository<CachableSwapiPerson> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class);

  @Autowired
  RedisTemplate<String, CachableSwapiPerson> redisTemplate;

  @Autowired
  Tracer tracer;

  @Override
  public void put(CachableSwapiPerson person) {
    redisTemplate.opsForHash().put(person.getObjectKey(), person.getKey(), person);
  }

  @Override
  public void delete(CachableSwapiPerson key) {
    redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
  }

  @Override
  public CachableSwapiPerson get(CachableSwapiPerson key) {
    //    return (CachableSwapiPerson) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
    Span newSpan = this.tracer.joinTrace("redis", this.tracer.getCurrentSpan());
    try {
      newSpan.logEvent("taxCalculated");
      return (CachableSwapiPerson) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
    } finally {
      // Once done remember to close the span. This will allow collecting
      // the span to send it to Zipkin
      this.tracer.close(newSpan);
    }
  }
}
