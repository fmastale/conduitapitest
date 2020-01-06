package com.griddynamics.conduit.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnprocessableEntityError {

  @JsonProperty("email or password")
  public String[] emailOrPassword;

  public String[] email;
  public String[] username;
  public String[] password;
}
