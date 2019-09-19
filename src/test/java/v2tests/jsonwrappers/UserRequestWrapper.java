package v2tests.jsonwrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import v2tests.jsons.UserRequest;

public class UserRequestWrapper {
  @JsonProperty("user")
  private UserRequest user;

  public UserRequestWrapper(UserRequest user) {
    this.user = user;
  }
}
