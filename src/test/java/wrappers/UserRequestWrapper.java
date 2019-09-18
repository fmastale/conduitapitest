package wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jsons.UserRequest;

public class UserRequestWrapper {
  @JsonProperty("user")
  private UserRequest userRequest;

  public UserRequest getUserRequest() {
    return userRequest;
  }

  public void setUserRequest(UserRequest userRequest) {
    this.userRequest = userRequest;
  }

  @Override
  public String toString() {
    return "UserRequestWrapper{" +
        "userRequest=" + userRequest +
        '}';
  }
}
