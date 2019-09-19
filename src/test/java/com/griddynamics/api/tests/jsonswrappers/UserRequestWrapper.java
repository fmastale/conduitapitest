package com.griddynamics.api.tests.jsonswrappers;

import com.griddynamics.api.tests.jsons.UserRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequestWrapper {
  @JsonProperty("user")
  private UserRequest user;

  public UserRequestWrapper(UserRequest user) {
    this.user = user;
  }
}
