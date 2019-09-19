package v1tests.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import v1tests.jsons.UserResponseBody;

public class UserResponseWrapper2 {
  @JsonProperty("user")
  public UserResponseBody userResponseBody;

  public UserResponseBody getUserResponseBody() {
    return userResponseBody;
  }

  public void setUserResponseBody(UserResponseBody userResponseBody) {
    this.userResponseBody = userResponseBody;
  }
}
