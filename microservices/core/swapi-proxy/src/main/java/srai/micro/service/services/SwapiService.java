package srai.micro.service.services;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import srai.micro.service.model.SwapiCharacter;

@Service
//@FeignClient(name = "swapi-service", url = "http://swapi.co/api/")
//@FeignClient(name = "swapi-service", url = "http://anapioficeandfire.com/api/")
@FeignClient(name = "swapi-service")
public interface SwapiService {
  @RequestMapping(method = RequestMethod.GET, value = "/api/people/{personId}")
  ResponseEntity<SwapiCharacter> getCharacter(@RequestHeader(value="User-Agent", defaultValue="curl/7.45.0") String userAgent, @PathVariable("personId") long personId);
}
