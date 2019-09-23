package com.griddynamics.conduit.jsonsdtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.griddynamics.conduit.jsons.UnprocessableEntityError;

public class UnprocessableEntityErrorDto {
  @JsonProperty("errors")
  public UnprocessableEntityError errors;
}
