package v1tests.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import v1tests.jsons.UserRequestBody;

public class UserRequestWrapper1 {
  @JsonProperty("user")
  public UserRequestBody userRequestBody;

  public UserRequestWrapper1(UserRequestBody userRequestBody) {
    this.userRequestBody = userRequestBody;
  }

  public UserRequestBody getUserRequestBody() {
    return userRequestBody;
  }

  public void setUserRequestBody(UserRequestBody userRequestBody) {
    this.userRequestBody = userRequestBody;
  }
}
