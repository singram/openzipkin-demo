package srai.micro.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import srai.common.micro.service.controller.ManagedResponseControllerBase;
import srai.micro.service.model.CachableSwapiCharacter;
import srai.micro.service.model.SwapiCharacter;
import srai.micro.service.model.repository.CharacterRepository;
import srai.micro.service.services.SwapiService;

@RestController
public class SwapiCharacterController extends ManagedResponseControllerBase {

  @Autowired
  CharacterRepository characterRepository;

  @Autowired
  SwapiService swapiService;

  @RequestMapping(value = "/person/{characterId}", method = RequestMethod.GET)
  @ResponseBody public ResponseEntity<?> getSwapiCharacter(@PathVariable long characterId) {
    logger.info("/person/{personId} called", characterId);
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/{personId} return the found person, processing time: {}", characterId, pt);

    // Check cache
    CachableSwapiCharacter cachedCharacter = new CachableSwapiCharacter(characterId);
    SwapiCharacter character = characterRepository.get(cachedCharacter);
    if (character == null) {
      // Retrieve from service
      ResponseEntity<SwapiCharacter> swapiCharacterResponse = swapiService.getCharacter(null, characterId);
      character = swapiCharacterResponse.getBody();
      if (character != null) {
        // Cache new character
        cachedCharacter = new CachableSwapiCharacter(characterId, character);
        characterRepository.put(cachedCharacter);
      }
    }

    if (character == null)
      return new ResponseEntity<SwapiCharacter>(character, HttpStatus.NOT_FOUND);
    else
      return new ResponseEntity<SwapiCharacter>(character, HttpStatus.OK);
  }


  @RequestMapping(value = "/person/", method = RequestMethod.POST)
  @ResponseBody public void savePerson(@RequestBody final CachableSwapiCharacter person) {
    logger.info("/person/ called");
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/ return the created person, processing time: {}", pt);
    characterRepository.put(person);
  }

}
