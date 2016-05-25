package srai.micro.service.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import srai.micro.service.model.CachableSwapiPerson;

@Service
public class PersonRepository implements Repository<CachableSwapiPerson> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class);

  @Autowired
  RedisTemplate<String, CachableSwapiPerson> redisTemplate;

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
    return (CachableSwapiPerson) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
  }
}
