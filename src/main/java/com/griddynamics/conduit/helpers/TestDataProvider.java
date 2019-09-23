package com.griddynamics.conduit.helpers;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import java.util.Locale;

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

  public RegistrationRequestUser getValidRegistrationUser() {
    FakeValuesService fakeValuesService =
        new FakeValuesService(new Locale("en-US"), new RandomService());

    String username = fakeValuesService.bothify("????##");
    String email = fakeValuesService.bothify("????##@mail.com");
    String password = fakeValuesService.bothify("????####");

    System.out.println(username + " " + email + " " + password);

    RegistrationRequestUser user = new RegistrationRequestUser(username, email, password);

    return user;
  }
}
