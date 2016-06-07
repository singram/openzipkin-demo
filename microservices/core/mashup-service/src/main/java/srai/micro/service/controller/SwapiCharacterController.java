package srai.micro.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.controller.ManagedResponseControllerBase;
import srai.micro.service.model.MashedCharacter;
import srai.micro.service.services.QuoteService;
import srai.micro.service.services.SwapiService;

@RestController
public class SwapiCharacterController extends ManagedResponseControllerBase {

  @Autowired
  SwapiService swapiService;

  @Autowired
  QuoteService quoteService;

  @RequestMapping(value = "/person/{characterId}", method = RequestMethod.GET)
  @ResponseBody public ResponseEntity<?> getSwapiCharacter(@PathVariable long characterId) {
    logger.info("/person/{personId} called", characterId);
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/{personId} return the found person, processing time: {}", characterId, pt);

    ResponseEntity<MashedCharacter> swapiCharacterResponse = swapiService.getCharacter(characterId);
    MashedCharacter character = swapiCharacterResponse.getBody();
    ResponseEntity<MashedCharacter> quoteResponse = quoteService.getQuote();

    if (character == null) {
      return new ResponseEntity<MashedCharacter>(character, HttpStatus.NOT_FOUND);
    }
    else {
      character.setTag_line(quoteResponse.getBody().getTag_line());
      return new ResponseEntity<MashedCharacter>(character, HttpStatus.OK);
    }
  }

}
