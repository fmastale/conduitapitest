package com.griddynamics.conduit.jsons;

public class UserRequest {
  public String email;
  public String password;
  public String username;
  public String bio;
  public String image;

  public UserRequest() {}

  public UserRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserRequest(String email, String password, String username) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public UserRequest(String email, String password, String username, String bio, String image) {
    this.email = email;
    this.password = password;
    this.username = username;
    this.bio = bio;
    this.image = image;
  }

  public UserRequest(String email) {
    this.email = email;
  }
}
