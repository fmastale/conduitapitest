package com.griddynamics.conduit.test.user;

import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UnprocessableEntityErrorDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import com.griddynamics.conduit.test.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

@Epic("Smoke tests")
@Feature("Registration")
public class RegistrationTest extends BaseTest {
  private int statusCode;
  private UserResponseDto responseBody;
  private UnprocessableEntityErrorDto errorBody;
  private RegistrationRequestUserDto requestBody;
  private RequestSpecification requestSpecification;
  private TestDataProvider testDataProvider = new TestDataProvider();

  @Severity(SeverityLevel.NORMAL)
  @Description("Register user with all fields valid, check if actual username is same as expected")
  @Test
  @DisplayName("Register valid user, check username")
  void registerValidUser() {

    for (RegistrationRequestUser user : testDataProvider.getValidUsers()) {
      // GIVEN
      prepareRequestBody(user);

      // WHEN
      responseBody = getApiCallResponseUser();

      // THEN
      MatcherAssert.assertThat(
          "Expected username is different than actual",
          responseBody.user.username,
          Matchers.equalTo(user.username));
    }
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Register user with taken username, check if actual error is same as expected")
  @Test
  @DisplayName("Register user with taken username, check error message")
  void cantRegisterUserWithIncorrectData() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithDuplicatedName());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than actual",
        errorBody.errors.username,
        Matchers.hasItemInArray("has already been taken"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Register user with empty username, check if actual error is same as expected")
  @Test
  @DisplayName("Register user with empty username, check error message")
  void cantRegisterUserWithEmptyUsername() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithEmptyName());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error messages are different than actual",
        errorBody.errors.username,
        Matchers.arrayContaining("can't be blank", "is too short (minimum is 1 character)"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user without specifying username, check if actual error is same as expected")
  @Test
  @DisplayName("Register user without username, check error message")
  void cantRegisterUserWithoutUsername() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithoutName());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error messages are different than actual",
        errorBody.errors.username,
        Matchers.arrayContaining(
            "can't be blank",
            "is too short (minimum is 1 character)",
            "is too long (maximum is 20 characters)"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user which username is max + 1 length, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with username length max + 1, check error message")
  void cantRegisterUserWithMaxPlusOneNameLength() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithMaxPlusName());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than actual",
        errorBody.errors.username,
        Matchers.hasItemInArray("is too long (maximum is 20 characters)"));
  }

  // I believe there is no such thing as too long email - I put 1800 chars into it and register user
  // minimal email format is '*@*.*' but most of them already used, so it will be hard to generate
  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with email in unusual format, check if actual username is same as expected")
  @Test
  @DisplayName("Register user with email in unusual format, check username match")
  void registerUserWithUnusualEmail() {

    for (RegistrationRequestUser user : testDataProvider.getUsersWithUnusualEmailFormat()) {
      // GIVEN
      prepareRequestBody(user);

      // WHEN
      responseBody = getApiCallResponseUser();

      // THEN
      MatcherAssert.assertThat(
          "Expected username is different than actual",
          responseBody.user.username,
          Matchers.equalTo(user.username));
    }
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with special characters in email, check if actual username is same as expected")
  @Test
  @DisplayName("Register user with special characters in email, check username")
  void registerUserWithSpecialCharsInEmail() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithSpecialCharsEmail());

    // WHEN
    responseBody = getApiCallResponseUser();

    // THEN
    MatcherAssert.assertThat(
        "Expected username is different than actual",
        responseBody.user.username,
        Matchers.equalTo(requestBody.user.username));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with space inside an email, check if actual error is same as expected")
  @Test
  @DisplayName("Register user with space inside email, check error message")
  void cantRegisterUserWithSpaceInEmail() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithSpaceInEmail());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than expected",
        errorBody.errors.email,
        Matchers.hasItemInArray("is invalid"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with email which is incorrectly formatted, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with email in incorrect format, check error message")
  void cantRegisterUserWithIncorrectEmailFormat() {

    for (RegistrationRequestUser user : testDataProvider.getUsersWithWrongEmailFormat()) {
      // GIVEN
      prepareRequestBody(user);

      // WHEN
      errorBody = getApiCallResponseError();

      // THEN
      MatcherAssert.assertThat(
          "Expected error message is different than expected",
          errorBody.errors.email,
          Matchers.hasItemInArray("is invalid"));
    }
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with email which is already taken, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with taken email, check error message")
  void cantRegisterUserWithTakenEmail() {
    // GIVEN
    RegistrationRequestUser registrationRequestUser = testDataProvider.getValidRegistrationUser();

    requestBody = new RegistrationRequestUserDto(registrationRequestUser);
    RestAssured.given().contentType(APPLICATION_JSON.get()).body(requestBody).post(USERS.get());

    prepareRequestBody(registrationRequestUser);

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        errorBody.errors.email,
        Matchers.hasItemInArray("has already been taken"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with empty email in request body, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with empty email, check error message")
  void cantRegisterUserWithEmptyEmail() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithEmptyEmail());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than expected",
        errorBody.errors.email,
        Matchers.hasItemInArray("can't be blank"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user without email in body, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user without email, check error message")
  void cantRegisterUserWithoutEmail() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithoutEmail());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than expected",
        errorBody.errors.email,
        Matchers.hasItemInArray("can't be blank"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Register user which use password with minimal length, check if status code is 200")
  @Test
  @DisplayName("Register user with minimal password length, check status code")
  void registerUserWithMinPassLength() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithMinPassLength());

    // WHEN
    statusCode = getApiCallResponseStatusCode();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with password length less than minimal, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with min-1 password length, check error message")
  void cantRegisterUserWithLessThanMinPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithLessThanMinPass());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Actual error message different than expected",
        errorBody.errors.password,
        Matchers.hasItemInArray("is too short (minimum is 8 characters)"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with empty password, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with empty password, check error message")
  void cantRegisterUserWithEmptyPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithEmptyPass());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Actual error is different than expected",
        errorBody.errors.password,
        Matchers.hasItemInArray("can't be blank"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user without specifying password, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user without password, check error message")
  void cantRegisterUserWithoutPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithoutPassword());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Actual error message is different than expected",
        errorBody.errors.password,
        Matchers.hasItemInArray("can't be blank"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Register user which use password with maximum length, check if status code is 200")
  @Test
  @DisplayName("Register user with max password length, check status code")
  void userWithMaxLengthPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithMaxLengthPass());

    // WHEN
    statusCode = getApiCallResponseStatusCode();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with password length higher than maximal, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with max + 1 password length, check error message")
  void cantRegisterUserWithTooLongPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithTooLongPass());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Actual error message is different than expected",
        errorBody.errors.password,
        Matchers.hasItemInArray("is too long (maximum is 72 characters)"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user which use special characters in  password, check if status code is 200")
  @Test
  @DisplayName("Register user with special characters in password, check status code")
  void registerUserWithRegularSpecialCharsInPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithSpecCharsInPass());

    // WHEN
    statusCode = getApiCallResponseStatusCode();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Register user with space inside password, check if status code is 200")
  @Test
  @DisplayName("Register user with space inside password, check status code")
  void registerUserWithSpaceInPass() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserInSpaceInPass());

    // WHEN
    statusCode = getApiCallResponseStatusCode();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Register user with empty body in request, check if actual error message is same as expected")
  @Test
  @DisplayName("Register user with empty body, check error message")
  void cantRegisterUserWithEmptyBody() {
    // GIVEN
    prepareRequestBody(new RegistrationRequestUser());

    // WHEN
    errorBody = getApiCallResponseError();

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        errorBody.errors.email,
        Matchers.hasItemInArray("can't be blank"));
  }

  private void prepareRequestBody(RegistrationRequestUser user) {
    requestBody = new RegistrationRequestUserDto(user);
    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.get()).body(requestBody);
  }

  private UserResponseDto getApiCallResponseUser() {
    return requestSpecification.post(USERS.get()).as(UserResponseDto.class);
  }

  private UnprocessableEntityErrorDto getApiCallResponseError() {
    return requestSpecification.post(USERS.get()).as(UnprocessableEntityErrorDto.class);
  }

  private int getApiCallResponseStatusCode() {
    return requestSpecification.post(USERS.get()).getStatusCode();
  }
}
