package srai.micro.service.services;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import srai.micro.service.model.SwapiCharacter;

@Service
@FeignClient(name = "swapi-service", url = "http://swapi-proxy:8080/")
public interface SwapiService {
  @RequestMapping(method = RequestMethod.GET, value = "/person/{personId}")
  ResponseEntity<SwapiCharacter> getCharacter(@PathVariable("personId") long personId);
}