package com.griddynamics.conduit.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnprocessableEntityError {
  @JsonProperty("email or password")
  public String[] emailOrPassword;

  @JsonProperty("email")
  public String[] email;

  @JsonProperty("username")
  public String[] username;
}
