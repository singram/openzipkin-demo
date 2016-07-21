package srai.micro.service.services;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import srai.micro.service.model.MashedCharacter;

@Service
//@FeignClient(name = "swapi-service", url = "${srai.micro.service.swapi-proxy.url}")
@FeignClient(name = "swapi-service")
public interface SwapiService {
  @RequestMapping(method = RequestMethod.GET, value = "/character/{personId}")
  ResponseEntity<MashedCharacter> getCharacter(@PathVariable("personId") long personId);
}