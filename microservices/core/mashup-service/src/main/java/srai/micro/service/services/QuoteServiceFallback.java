package srai.micro.service.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import srai.micro.service.model.MashedCharacter;

@Service
class QuoteServiceFallback implements QuoteService {
  @Override
  public ResponseEntity<MashedCharacter> getQuote() {
    MashedCharacter c = new MashedCharacter();
    c.setTag_line("QUOTE SERVICE FAILED");
    ResponseEntity<MashedCharacter> re = new ResponseEntity<MashedCharacter>(c, HttpStatus.GATEWAY_TIMEOUT);
    return re;
  }
}
