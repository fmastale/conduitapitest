package wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jsons.UserResponse;

public class UserResponseWrapper {
  @JsonProperty("user")
  public UserResponse userResponse;

  public UserResponse getUserResponse() {
    return userResponse;
  }

  public void setUserResponse(UserResponse userResponse) {
    this.userResponse = userResponse;
  }
}
