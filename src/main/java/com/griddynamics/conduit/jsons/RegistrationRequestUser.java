package com.griddynamics.conduit.jsons;

public class RegistrationRequestUser {
  public String username;
  public String email;
  public String password;

  public RegistrationRequestUser(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public RegistrationRequestUser() {

  }
}
