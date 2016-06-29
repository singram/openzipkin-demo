package srai.micro.service.services;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import srai.micro.service.model.MashedCharacter;

@Service
//@FeignClient(name = "quote-service", url = "${srai.micro.service.quote-service.url}")
@FeignClient("quote-service")
public interface QuoteService {
  @RequestMapping(method = RequestMethod.GET, value = "/")
  ResponseEntity<MashedCharacter> getQuote();
}

