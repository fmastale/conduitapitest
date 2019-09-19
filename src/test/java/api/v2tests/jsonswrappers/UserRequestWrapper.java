package api.v2tests.jsonswrappers;

import api.v2tests.jsons.UserRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequestWrapper {
  @JsonProperty("user")
  private UserRequest user;

  public UserRequestWrapper(UserRequest user) {
    this.user = user;
  }
}
