package srai.micro.service.model;

public class CachableSwapiCharacter extends SwapiCharacter implements Cachable {

  private static final long serialVersionUID = 347915415730767126L;

  public static final String OBJECT_KEY = "SWAPI_CHARACTER";

  public CachableSwapiCharacter(long characterId, SwapiCharacter p) {
    setId(characterId);
    setEye_color(p.getEye_color());
    setName(p.getName());
    setGender(p.getGender());
  }

  /** Primary id. */
  private Long id;

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  public CachableSwapiCharacter() {
    super();
  }

  public CachableSwapiCharacter(long id) {
    super();
    setId(id);
  }

  @Override
  public String getKey() {
    return String.valueOf(getId());
  }

  @Override
  public String getObjectKey() {
    return OBJECT_KEY;
  }

}
