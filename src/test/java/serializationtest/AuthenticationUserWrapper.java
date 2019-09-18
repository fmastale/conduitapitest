package serializationtest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationUserWrapper {
  @JsonProperty("User")
  private AuthenticationUser autenticationUser;

  public AuthenticationUser getAutenticationUser() {
    return autenticationUser;
  }

  public void setAutenticationUser(AuthenticationUser autenticationUser) {
    this.autenticationUser = autenticationUser;
  }

  @Override
  public String toString() {
    return "AutenticationUserWrapper{" +
        "autenticationUser=" + autenticationUser +
        '}';
  }
}
