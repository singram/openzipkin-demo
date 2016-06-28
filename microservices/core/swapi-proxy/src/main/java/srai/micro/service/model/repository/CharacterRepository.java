package srai.micro.service.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import srai.micro.service.model.CachableSwapiCharacter;

@Service
public class CharacterRepository implements Repository<CachableSwapiCharacter> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CharacterRepository.class);

  @Autowired
  RedisTemplate<String, CachableSwapiCharacter> redisTemplate;

  //  @Autowired
  //  Tracer tracer;

  @Override
  public void put(CachableSwapiCharacter character) {
    //    Span newSpan = this.tracer.createSpan("redis");
    try {
      //      newSpan.tag("Operation", "write");
      redisTemplate.opsForHash().put(character.getObjectKey(), character.getKey(), character);
    } finally {
      //      this.tracer.close(newSpan);
    }
  }

  @Override
  public void delete(CachableSwapiCharacter key) {
    //    Span newSpan = this.tracer.createSpan("redis");
    try {
      //      newSpan.tag("Operation", "delete");
      redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
    } finally {
      //      this.tracer.close(newSpan);
    }
  }

  @Override
  public CachableSwapiCharacter get(CachableSwapiCharacter key) {
    //    Span newSpan = this.tracer.createSpan("redis");
    try {
      //      newSpan.tag("Operation", "read");
      return (CachableSwapiCharacter) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
    } finally {
      //      this.tracer.close(newSpan);
    }
  }
}
