package com.griddynamics.api.tests.jsonswrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.griddynamics.api.tests.jsons.UserRequest;

public class UserRequestWrapper {
  @JsonProperty
  private UserRequest user;

  public UserRequestWrapper(UserRequest user) {
    this.user = user;
  }
}
