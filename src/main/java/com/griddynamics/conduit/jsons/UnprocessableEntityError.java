package com.griddynamics.conduit.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnprocessableEntityError {
  @JsonProperty("email or password")
  public String[] emailOrPassword;
}
