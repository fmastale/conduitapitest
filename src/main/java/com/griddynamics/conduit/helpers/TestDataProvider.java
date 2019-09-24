package com.griddynamics.conduit.helpers;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TestDataProvider {
  private String username;
  private String maxLengthName;
  private String nameWithNumbers;
  private String usernameWithSpace;
  private String nameWithSpecialChars;

  private String email;

  private String password;
  private String incorrectPassword;

  private FakeValuesService fakeValuesService =
      new FakeValuesService(new Locale("en-US"), new RandomService());

  public TestDataProvider() {
    // todo: move declaration + creation to get methods?
    this.username = getNewUsername();
    this.maxLengthName = fakeValuesService.regexify("[a-zA-Z1-9]{20}");
    this.nameWithNumbers = fakeValuesService.numerify("##########");
    this.nameWithSpecialChars = fakeValuesService.regexify("[^a-zA-Z0-9_]{10}");
    this.usernameWithSpace = fakeValuesService.bothify("????# ????#");

    this.email = fakeValuesService.bothify("????##@mail.com");

    this.password = fakeValuesService.bothify("????####");
    this.incorrectPassword = fakeValuesService.bothify("####????");
  }

  public String getIncorrectPassword() {
    return incorrectPassword;
  }

  public RegistrationRequestUser getValidRegistrationUser() {
    return new RegistrationRequestUser(username, email, password);
  }

  public String getMaxPlusOneUsername() {
    return maxLengthName + fakeValuesService.regexify("[a-zA-Z1-9]{1}");
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
    return new RegistrationRequestUser(maxLengthName, email, password);
  }

  public RegistrationRequestUser getUserWithMaxPlusName() {
    return new RegistrationRequestUser(getMaxPlusOneUsername(), email, password);
  }

  public String getRandomIncorrectUsername() {
    return fakeValuesService.bothify("?#?#?#?#?#?#?#?#?#?#");
  }

  public List<RegistrationRequestUser> getValidUsers() {

    RegistrationRequestUser[] users = {
      // todo: special chars which? all (\`>@攫`@`?) or regular ($%^&(***&^)?
      // new RegistrationRequestUser(nameWithSpecialChars, getNewEmail(), password),
      new RegistrationRequestUser(maxLengthName, getNewEmail(), password),
      new RegistrationRequestUser(usernameWithSpace, getNewEmail(), password),
      new RegistrationRequestUser(nameWithNumbers, getNewEmail(), password),
    };

    List<RegistrationRequestUser> validUserList = Arrays.asList(users);
    return validUserList;
  }

  public List<RegistrationRequestUser> getUsersWithWrongEmailFormat() {

    // todo: different incorrect formats?!
    // ? == letter, # == number
    RegistrationRequestUser[] array = {
      new RegistrationRequestUser(
          getNewUsername(), fakeValuesService.regexify("[a-zA-z1-9]{10}"), password),
      new RegistrationRequestUser(getNewUsername(), fakeValuesService.bothify("@???.??"), password),
      new RegistrationRequestUser(
          getNewUsername(), fakeValuesService.bothify("????##@??."), password)
    };

    List<RegistrationRequestUser> usersWithWrongEmailFormat = Arrays.asList(array);
    return usersWithWrongEmailFormat;
  }

  public List<RegistrationRequestUser> getUsersWithStrangeEmailFormat() {

    // todo: add and test email in format: '[\W]{5}@mail.com' -> '!#$%^&@mail.com'
    // ? == letter, # == number
    RegistrationRequestUser[] array = {
      new RegistrationRequestUser(
          getNewUsername(), fakeValuesService.bothify("?????@???.###"), password),
      new RegistrationRequestUser(
          getNewUsername(), fakeValuesService.bothify("?????@###.???"), password)
    };

    List<RegistrationRequestUser> usersWithStrangeEmailFormat = Arrays.asList(array);
    return usersWithStrangeEmailFormat;
  }

  private String getNewUsername() {
    return fakeValuesService.bothify("????????##");
  }

  private String getNewEmail() {
    return fakeValuesService.bothify("????##@mail.com");
  }

  public RegistrationRequestUser getUserWithSpaceInEmail() {
    return new RegistrationRequestUser(
        getNewUsername(), fakeValuesService.bothify("????? ???@mail.com"), password);
  }

  public RegistrationRequestUser getUserWithEmptyEmail() {
    return new RegistrationRequestUser(getNewUsername(), "", password);
  }
}
