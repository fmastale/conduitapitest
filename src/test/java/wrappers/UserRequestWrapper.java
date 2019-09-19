package wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jsons.UserRequest;

public class UserRequestWrapper {
  @JsonProperty("user")
  public UserRequest userRequest;

  public UserRequestWrapper(UserRequest userRequest) {
    this.userRequest = userRequest;
  }

  public UserRequest getUserRequest() {
    return userRequest;
  }

  public void setUserRequest(UserRequest userRequest) {
    this.userRequest = userRequest;
  }
}
