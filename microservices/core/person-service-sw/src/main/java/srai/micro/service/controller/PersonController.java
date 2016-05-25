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
import srai.micro.service.model.CachableSwapiPerson;
import srai.micro.service.model.SwapiPerson;
import srai.micro.service.model.repository.PersonRepository;
import srai.micro.service.services.SwapiService;

@RestController
public class PersonController extends ManagedResponseControllerBase {

  @Autowired
  PersonRepository personRepository;

  @Autowired
  SwapiService swPersonService;


  @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
  @ResponseBody public ResponseEntity<?> getSWPerson(@PathVariable long personId) {
    logger.info("/swperson/{personId} called", personId);
    final int pt = controlResponseTimeAndError();
    logger.debug("/swperson/{personId} return the found person, processing time: {}", personId, pt);

    // Check cache
    CachableSwapiPerson cachedPerson = new CachableSwapiPerson(personId);
    SwapiPerson person = personRepository.get(cachedPerson);
    if (person == null) {
      // Retrieeve from service
      ResponseEntity<SwapiPerson> swPersonResponse = swPersonService.getPerson(null, personId);
      person = swPersonResponse.getBody();
      if (person != null) {
        // Cache new character
        cachedPerson = new CachableSwapiPerson(personId, person);
        personRepository.put(cachedPerson);
      }
    }

    if (person == null)
      return new ResponseEntity<SwapiPerson>(person, HttpStatus.NOT_FOUND);
    else
      return new ResponseEntity<SwapiPerson>(person, HttpStatus.OK);
  }


  @RequestMapping(value = "/person/", method = RequestMethod.POST)
  @ResponseBody public void savePerson(@RequestBody final CachableSwapiPerson person) {
    logger.info("/person/ called");
    final int pt = controlResponseTimeAndError();
    logger.debug("/person/ return the created person, processing time: {}", pt);
    personRepository.put(person);
  }

}
