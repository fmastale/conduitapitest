package com.griddynamics.conduit.jsons;

import java.util.Objects;

public class UserRequest {
  public String email;
  public String password;
  public String username;
  public String bio;
  public String image;

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

  public UserRequest(String email) {}

  public UserRequest() {}

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRequest that = (UserRequest) o;
    return Objects.equals(email, that.email) &&
        Objects.equals(password, that.password) &&
        Objects.equals(username, that.username) &&
        Objects.equals(bio, that.bio) &&
        Objects.equals(image, that.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, username, bio, image);
  }

  @Override
  public String toString() {
    return "UserRequest{" +
        "email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", username='" + username + '\'' +
        ", bio='" + bio + '\'' +
        ", image='" + image + '\'' +
        '}';
  }
}
