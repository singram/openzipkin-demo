package srai.micro.service.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

import java.io.IOException;

@RestController
public class SwapiCharacterController extends ManagedResponseControllerBase {

  @Autowired
  CharacterRepository characterRepository;

  @Autowired
  SwapiService swapiService;

  @SuppressWarnings("unused")
  @RequestMapping(value = "/person/{characterId}", method = RequestMethod.GET)
  @ResponseBody public ResponseEntity<?> getSwapiCharacter(@PathVariable long characterId) throws IOException {
    logger.info("/person/{personId} called", characterId);
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/{personId} return the found person, processing time: {}", characterId, pt);

    // Check cache
    //    CachableSwapiCharacter cachedCharacter = new CachableSwapiCharacter(characterId);
    //    SwapiCharacter character = characterRepository.get(cachedCharacter);
    //    if (character == null) {
    //      // Retrieve from service
    //      ResponseEntity<SwapiCharacter> swapiCharacterResponse = swapiService.getCharacter(null, characterId);
    //      character = swapiCharacterResponse.getBody();
    //      if (character != null) {
    //        // Cache new character
    //        cachedCharacter = new CachableSwapiCharacter(characterId, character);
    //        characterRepository.put(cachedCharacter);
    //      }
    //    }
    ResponseEntity<SwapiCharacter> swapiCharacterResponse = swapiService.getCharacter(null, characterId);
    SwapiCharacter character = swapiCharacterResponse.getBody();
    //SwapiCharacter character = new SwapiCharacter();

    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("http://www.google.com");
    CloseableHttpResponse response1 = null;
    try {
      response1 = httpclient.execute(httpGet);
      System.out.println(response1.getStatusLine());
      HttpEntity entity1 = response1.getEntity();
      character.setName(entity1.getContent().toString());
      EntityUtils.consume(entity1);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (UnsupportedOperationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      response1.close();
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
