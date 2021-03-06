package srai.micro.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapiCharacter implements Serializable {

  private static final long serialVersionUID = 8672943541016167613L;

  private String name;

  private String gender;

  private String eye_color;

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "SwapiPerson{" +
        "name='" + name + '\'' +
        '}';
  }

  /**
   * @return the gender
   */
  public String getGender() {
    return gender;
  }

  /**
   * @param gender the gender to set
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * @return the eye_color
   */
  public String getEye_color() {
    return eye_color;
  }

  /**
   * @param eye_color the eye_color to set
   */
  public void setEye_color(String eye_color) {
    this.eye_color = eye_color;
  }

}

