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
  private String usernameWithSpace;

  private String email;

  private String password;
  private String incorrectPassword;

  private FakeValuesService fakeValuesService =
      new FakeValuesService(new Locale("en-US"), new RandomService());

  public TestDataProvider() {
    this.username = getNewUsername();
    this.maxLengthName = fakeValuesService.regexify("[a-zA-Z1-9]{20}");
    // todo: regex with space inside
    this.usernameWithSpace = fakeValuesService.bothify("????# ????#");

    this.email = getNewEmail();

    this.password = getNewPassword();
    this.incorrectPassword = fakeValuesService.regexify("[a-zA-Z1-9]{7}");
  }

  public String getIncorrectPassword() {
    return incorrectPassword;
  }

  public String getMaxPlusOneUsername() {
    return maxLengthName + fakeValuesService.regexify("[a-zA-Z1-9]{1}");
  }

  public String getRandomIncorrectUsername() {
    return getNewUsername();
  }

  public String getSpecialCharsEmail() {
    return specialCharsString() + "@mail.com";
  }

  public RegistrationRequestUser getValidRegistrationUser() {
    return new RegistrationRequestUser(username, email, password);
  }

  public RegistrationRequestUser getUserWithDuplicatedName() {
    return new RegistrationRequestUser("adam1234io", email, password);
  }

  public RegistrationRequestUser getUserWithEmptyName() {
    return new RegistrationRequestUser("", email, password);
  }

  public RegistrationRequestUser getUserWithoutName() {
    return new RegistrationRequestUser(null, email, password);
  }

  public RegistrationRequestUser getUserWithMaxName() {
    return new RegistrationRequestUser(maxLengthName, email, password);
  }

  public RegistrationRequestUser getUserWithMaxPlusName() {
    return new RegistrationRequestUser(getMaxPlusOneUsername(), email, password);
  }

  public RegistrationRequestUser getUserWithSpaceInEmail() {
    return new RegistrationRequestUser(
        getNewUsername(), fakeValuesService.bothify("????? ???@mail.com"), password);
  }

  public RegistrationRequestUser getUserWithEmptyEmail() {
    return new RegistrationRequestUser(getNewUsername(), "", password);
  }

  public RegistrationRequestUser getUserWithSpecialCharsEmail() {
    return new RegistrationRequestUser(getNewUsername(), getSpecialCharsEmail(), password);
  }

  public RegistrationRequestUser getUserWithoutEmail() {
    return new RegistrationRequestUser(getNewUsername(), null, password);
  }

  public RegistrationRequestUser getUserWithMinPassLength() {
    return new RegistrationRequestUser(
        getNewUsername(), getNewEmail(), fakeValuesService.regexify("[a-zA-Z1-9]{8}"));
  }

  public RegistrationRequestUser getUserWithLessThanMinPass() {
    return new RegistrationRequestUser(
        getNewUsername(), getNewEmail(), fakeValuesService.regexify("[a-zA-Z1-9]{7}"));
  }

  public RegistrationRequestUser getUserWithEmptyPass() {
    return new RegistrationRequestUser(getNewUsername(), getNewEmail(), "");
  }

  public RegistrationRequestUser getUserWithoutPassword() {
    return new RegistrationRequestUser(getNewUsername(), getNewEmail(), null);
  }

  public RegistrationRequestUser getUserWithMaxLengthPass() {
    return new RegistrationRequestUser(
        getNewUsername(), getNewEmail(), fakeValuesService.regexify("[a-zA-Z1-9]{72}"));
  }

  public RegistrationRequestUser getUserWithTooLongPass() {
    return new RegistrationRequestUser(getNewUsername(), getNewEmail(), fakeValuesService.regexify("[a-zA-Z1-9]{73}"));
  }

  public RegistrationRequestUser getUserWithSpecCharsInPass() {
    return new RegistrationRequestUser(getNewUsername(), getNewEmail(), specialCharsString());
  }

  public RegistrationRequestUser getUserInSpaceInPass() {
    return new RegistrationRequestUser(getNewUsername(), getNewEmail(), fakeValuesService.bothify("##### ?????"));
  }


  public List<RegistrationRequestUser> getValidUsers() {

    RegistrationRequestUser[] users = {
      new RegistrationRequestUser(getNameWithSpecialChars(), getNewEmail(), password),
      new RegistrationRequestUser(maxLengthName, getNewEmail(), password),
      new RegistrationRequestUser(usernameWithSpace, getNewEmail(), password),
    };

    List<RegistrationRequestUser> validUserList = Arrays.asList(users);
    return validUserList;
  }

  public List<RegistrationRequestUser> getUsersWithWrongEmailFormat() {

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

    // todo: search for valid but unusual email formats
    RegistrationRequestUser[] array = {
      new RegistrationRequestUser(
          getNewUsername(), fakeValuesService.bothify("?????@???.###"), password),
      new RegistrationRequestUser(
          getNewUsername(), fakeValuesService.bothify("?????@###.???"), password)
    };

    List<RegistrationRequestUser> usersWithStrangeEmailFormat = Arrays.asList(array);
    return usersWithStrangeEmailFormat;
  }

  private String getNameWithSpecialChars() {
    return specialCharsString();
  }

  private String getNewUsername() {
    return fakeValuesService.regexify("[a-zA-Z1-9]{15}");
  }

  private String getNewEmail() {
    return getNewUsername() + "@mail.com";
  }

  private String specialCharsString() {
    return fakeValuesService.regexify("['?/!#$%^&*()]{15}");
  }

  private String getNewPassword() {
    return fakeValuesService.regexify("[a-zA-Z1-9]{10}");
  }
}
