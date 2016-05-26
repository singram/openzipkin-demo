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
    Span newSpan = this.tracer.createSpan("redis");
    try {
      newSpan.tag("Operation", "write");
      redisTemplate.opsForHash().put(person.getObjectKey(), person.getKey(), person);
    } finally {
      this.tracer.close(newSpan);
    }
  }

  @Override
  public void delete(CachableSwapiPerson key) {
    Span newSpan = this.tracer.createSpan("redis");
    try {
      newSpan.tag("Operation", "delete");
      redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
    } finally {
      this.tracer.close(newSpan);
    }
  }

  @Override
  public CachableSwapiPerson get(CachableSwapiPerson key) {
    Span newSpan = this.tracer.createSpan("redis");
    try {
      newSpan.tag("Operation", "read");
      return (CachableSwapiPerson) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
    } finally {
      this.tracer.close(newSpan);
    }
  }
}
