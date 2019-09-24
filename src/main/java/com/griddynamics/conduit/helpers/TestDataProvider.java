package com.griddynamics.conduit.helpers;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
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
  private List<RegistrationRequestUser> usersWithWrongEmailFormat;
  private List<RegistrationRequestUser> usersWithStrangeEmailFormat;
  private FakeValuesService fakeValuesService =
      new FakeValuesService(new Locale("en-US"), new RandomService());

  public TestDataProvider() {
    // todo: move declaration + creation to get methods?
    this.username = createNewUsername();
    this.email = fakeValuesService.bothify("????##@mail.com");
    this.password = fakeValuesService.bothify("????####");
    this.bio = fakeValuesService.regexify("[a-zA-Z1-9]{30}");
    this.updatedBio = fakeValuesService.regexify("[a-zA-Z1-9]{30}");
    this.incorrectPassword = fakeValuesService.bothify("####????");
    this.maxUsername = fakeValuesService.regexify("[a-zA-Z1-9]{20}");
    this.usersWithWrongEmailFormat = createUsersWithWrongEmailFormat();
    this.usersWithStrangeEmailFormat = createUsersWithStrangeEmailFormat();
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

  public List<RegistrationRequestUser> getUserWithWrongEmailFormat() {
    return usersWithWrongEmailFormat;
  }

  public RegistrationRequestUser getUserWithDuplicatedName() {
    return new RegistrationRequestUser("adam1234io", email, password);
  }

  public RegistrationRequestUser getUserWithEmptyName() {
    return new RegistrationRequestUser("", email, password);
  }

  public RegistrationRequestUser getUserWithNullName() {
    return new RegistrationRequestUser(email, password);
  }

  public RegistrationRequestUser getUserWithMaxName() {
    return new RegistrationRequestUser(maxUsername, email, password);
  }

  public RegistrationRequestUser getUserWithMaxPlusName() {
    return new RegistrationRequestUser(getMaxPlusOneUsername(), email, password);
  }

  public List<RegistrationRequestUser> getUserWithStrangeEmail() {
    return usersWithStrangeEmailFormat;
  }

  private List<RegistrationRequestUser> createUsersWithWrongEmailFormat() {

    // todo: different incorrect formats?!
    // ? == letter, # == number
    RegistrationRequestUser[] array = {
      new RegistrationRequestUser(
          createNewUsername(), fakeValuesService.regexify("[a-zA-z1-9]{10}"), password),
      new RegistrationRequestUser(
          createNewUsername(), fakeValuesService.bothify("@???.??"), password),
      new RegistrationRequestUser(
          createNewUsername(), fakeValuesService.bothify("????##@??."), password)
    };
    usersWithWrongEmailFormat = Arrays.asList(array);

    return usersWithWrongEmailFormat;
  }

  private List<RegistrationRequestUser> createUsersWithStrangeEmailFormat() {

    // todo: test email in format: '[\W]{5}@mail.com' -> '!#$%^&@mail.com'
    // ? == letter, # == number
    RegistrationRequestUser[] array = {
      new RegistrationRequestUser(
          createNewUsername(), fakeValuesService.bothify("?????@???.###"), password),
      new RegistrationRequestUser(
          createNewUsername(), fakeValuesService.bothify("?????@###.???"), password)
    };

    return usersWithStrangeEmailFormat = Arrays.asList(array);
  }

  private String createNewUsername() {
    return fakeValuesService.bothify("????##");
  }
}
