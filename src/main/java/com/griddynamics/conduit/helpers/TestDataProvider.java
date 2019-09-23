package com.griddynamics.conduit.helpers;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TestDataProvider {

  private String email;
  private String password;
  private String username;
  private String bio;
  private String updatedBio;
  private String maxUsername;
  private String incorrectPassword;
  private List<String> incorrectlyFormattedEmails;
  private FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());

  public TestDataProvider() {
    this.username = fakeValuesService.bothify("????##");
    this.email = fakeValuesService.bothify("????##@mail.com");
    this.password = fakeValuesService.bothify("????####");
    this.bio = fakeValuesService.regexify("[a-zA-Z1-9]{30}");
    this.updatedBio = fakeValuesService.regexify("[a-zA-Z1-9]{30}");
    this.incorrectPassword = fakeValuesService.bothify("####????");
    this.maxUsername = fakeValuesService.regexify("[a-zA-Z1-9]{20}");
    this.incorrectlyFormattedEmails = generateIncorrectlyFormattedEmails();
  }

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
    return new RegistrationRequestUser(username, email, password);
  }

  public String getMaxUsername() {
    return maxUsername;
  }

  public String getMaxPlusOneUsername() {
    return maxUsername + fakeValuesService.regexify("[a-zA-Z1-9]{1}");
  }

  public List<String> getIncorrectlyFormattedEmails() {
    return incorrectlyFormattedEmails;
  }

  private List<String> generateIncorrectlyFormattedEmails() {
    //todo: different incorrect formats?!

    // ? == letter, # == number
    // app allow to make email in format '????@??.###
    String[] array = {
        fakeValuesService.regexify("[a-zA-z1-9]{10}"),
        fakeValuesService.bothify("@???.??"),
        fakeValuesService.bothify("????##@??.")
    };

    incorrectlyFormattedEmails = Arrays.asList(array);

    return incorrectlyFormattedEmails;
  }
}

