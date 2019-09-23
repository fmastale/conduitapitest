package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UnprocessableEntityErrorDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
  private Response response;
  private UserResponseDto responseBody;
  private RegistrationRequestUser user;
  private UnprocessableEntityErrorDto errorBody;
  private RegistrationRequestUserDto requestBody;
  private RequestSpecification requestSpecification;
  private TestDataProvider testDataProvider = new TestDataProvider();

  @BeforeAll
  static void beforeAll() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
  }

  @Test
  @DisplayName("Register user with valid data and check if usernames are equal")
  void registerUserWithValidData() {
    // GIVEN
    user = testDataProvider.getValidRegistrationUser();
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    responseBody = response.as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat("", responseBody.user.username, Matchers.equalTo(user.username));
  }

  @Test
  @DisplayName("Register user with username which is already taken, then check error message")
  void registerUserWithIncorrectData() {
    // GIVEN
    user =
        new RegistrationRequestUser(
            "adam1234io", testDataProvider.getEmail(), testDataProvider.getPassword());
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    errorBody = response.as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than actual",
        errorBody.errors.username,
        Matchers.hasItemInArray("has already been taken"));
  }

  @Test
  @DisplayName("Register user with empty username, check error message")
  void registerUserWithEmptyUsername() {
    // GIVEN
    user =
        new RegistrationRequestUser(
            "", testDataProvider.getEmail(), testDataProvider.getPassword());
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    errorBody = response.as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected error messages are different than actual",
        errorBody.errors.username,
        Matchers.arrayContaining("can't be blank", "is too short (minimum is 1 character)"));
  }

  @Test
  @DisplayName("Register user with 20 chars username, check usernames match")
  void registerUserWithMaxUsernameLength() {
    // GIVEN
    user =
        new RegistrationRequestUser(
            testDataProvider.getMaxUsername(),
            testDataProvider.getEmail(),
            testDataProvider.getPassword());

    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    UserResponseDto responseBody = response.as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected username is different than actual",
        responseBody.user.username,
        Matchers.equalTo(user.username));
  }

  @Test
  @DisplayName("Register user with 21 chars username, check error message")
  void registerUserWithMaxPlusOneUsernameLength() {
    // GIVEN
    user =
        new RegistrationRequestUser(
            testDataProvider.getMaxPlusOneUsername(),
            testDataProvider.getEmail(),
            testDataProvider.getPassword());

    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    errorBody = response.as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected error message is different than actual",
        errorBody.errors.username,
        Matchers.hasItemInArray("is too long (maximum is 20 characters)"));
  }

  @Test
  @DisplayName("Register user with email which is incorrectly formatted, check error message")
  void registerUserWithIncorrectlyFormattedEmail() {

    // todo: use iteration over array or split and put each invalid format to different test case
    //  method?
    List<String> incorrectEmails = testDataProvider.getIncorrectlyFormattedEmails();

    for (int i = 0; i < incorrectEmails.size(); i++) {
      // GIVEN
      user =
          new RegistrationRequestUser(
              testDataProvider.getUsername(),
              testDataProvider.getIncorrectlyFormattedEmails().get(i),
              testDataProvider.getPassword());

      requestBody = new RegistrationRequestUserDto(user);

      requestSpecification =
          RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

      // WHEN
      response = requestSpecification.post(USERS.getEndpoint());
      errorBody = response.as(UnprocessableEntityErrorDto.class);

      // THEN
      MatcherAssert.assertThat(
          "Expected error message is different than expected",
          errorBody.errors.email,
          Matchers.hasItemInArray("is invalid"));
    }
  }

  @Test
  @DisplayName("Register user with email which is already taken, check error message")
  void registerUserWithTakenEmailAddress() {
    // GIVEN
    registerUser();

    user = testDataProvider.getValidRegistrationUser();
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    UnprocessableEntityErrorDto responseError = response.as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        responseError.errors.email,
        Matchers.hasItemInArray("has already been taken"));
  }

  @Test
  @DisplayName("Register user with empty body, check error message")
  void registerUserWithEmptyBody() {
    //GIVEN
    user = new RegistrationRequestUser();
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    UnprocessableEntityErrorDto responseError = response.as(UnprocessableEntityErrorDto.class);

    //THEN
    MatcherAssert.assertThat("Expected and actual error messages are not the same",
        responseError.errors.email, Matchers.hasItemInArray("can't be blank"));

  }

  private void registerUser() {
    user = testDataProvider.getValidRegistrationUser();
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    response = requestSpecification.post(USERS.getEndpoint());
    responseBody = response.as(UserResponseDto.class);
  }
}
