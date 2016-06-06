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
import srai.micro.service.model.SwapiCharacter;
import srai.micro.service.services.SwapiService;

@RestController
public class SwapiCharacterController extends ManagedResponseControllerBase {

  @Autowired
  SwapiService swapiService;

  @RequestMapping(value = "/person/{characterId}", method = RequestMethod.GET)
  @ResponseBody public ResponseEntity<?> getSwapiCharacter(@PathVariable long characterId) {
    logger.info("/person/{personId} called", characterId);
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/{personId} return the found person, processing time: {}", characterId, pt);

    ResponseEntity<SwapiCharacter> swapiCharacterResponse = swapiService.getCharacter(characterId);
    SwapiCharacter character = swapiCharacterResponse.getBody();

    if (character == null)
      return new ResponseEntity<SwapiCharacter>(character, HttpStatus.NOT_FOUND);
    else
      return new ResponseEntity<SwapiCharacter>(character, HttpStatus.OK);
  }

}
