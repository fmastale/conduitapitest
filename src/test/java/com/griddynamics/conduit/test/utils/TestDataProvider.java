package com.griddynamics.conduit.test.utils;

public class TestDataProvider {
  private String email = "adam@mail.com";
  private String password = "adam1234";
  private String username = "adam1234io";
  private String bio = "I like to eat cookies";
  private String updatedBio = "I like to ride on skateboard";
  private String incorrectPassword = "thisPasswordIsNotValid";

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public String getBio() {
    return bio;
  }

  public String getUpdatedBio() {
    return updatedBio;
  }

  public String getIncorrectPassword() {
    return incorrectPassword;
  }
}
