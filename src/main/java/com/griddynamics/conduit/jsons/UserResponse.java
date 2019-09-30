package com.griddynamics.conduit.jsons;

public class UserResponse {
  public int id;
  public String email;
  public String createdAt;
  public String updatedAt;
  public String username;
  public String bio;
  public String image;
  public String token;

  @Override
  public String toString() {
    return "UserResponse{" +
        "id=" + id +
        ", email='" + email + '\'' +
        ", createdAt='" + createdAt + '\'' +
        ", updatedAt='" + updatedAt + '\'' +
        ", username='" + username + '\'' +
        ", bio='" + bio + '\'' +
        ", image='" + image + '\'' +
        ", token='" + token + '\'' +
        '}';
  }
}
