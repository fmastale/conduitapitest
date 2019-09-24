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
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
  private UserResponseDto responseBody;
  private UnprocessableEntityErrorDto errorBody;
  private RegistrationRequestUserDto requestBody;
  private RequestSpecification requestSpecification;
  private TestDataProvider testDataProvider = new TestDataProvider();

  @BeforeAll
  static void beforeAll() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
  }

  @Test
  @DisplayName("Register user with valid data, check if username match")
  void registerUserWithValidData() {
    // GIVEN
    prepareRequestBody(testDataProvider.getValidRegistrationUser());

    // WHEN
    responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected username is different than actual",
        responseBody.user.username,
        Matchers.equalTo(requestBody.user.username));
  }

  @Test
  @DisplayName("Register user with username which is already taken, check error message")
  void registerUserWithIncorrectData() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithDuplicatedName());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

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
    prepareRequestBody(testDataProvider.getUserWithEmptyName());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected error messages are different than actual",
        errorBody.errors.username,
        Matchers.arrayContaining("can't be blank", "is too short (minimum is 1 character)"));
  }

  // todo: this test is giving me back error according to too long username
  @Test
  @DisplayName("Register user without specifying username, check error message")
  void registerUserWithoutUsername() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithNullName());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected error messages are different than actual",
        errorBody.errors.username,
        Matchers.hasItemInArray("is too long (maximum is 20 characters)"));
  }

  @Test
  @DisplayName("Register user with max chars username, check username match")
  void registerUserWithMaxUsernameLength() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithMaxName());

    // WHEN
    responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected username is different than actual",
        responseBody.user.username,
        Matchers.equalTo(requestBody.user.username));
  }

  @Test
  @DisplayName("Register user with max+1 chars username, check error message")
  void registerUserWithMaxPlusOneUsernameLength() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithMaxPlusName());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

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
    for (RegistrationRequestUser user : testDataProvider.getUserWithWrongEmailFormat()) {
      // GIVEN
      prepareRequestBody(user);

      // WHEN
      errorBody =
          requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

      // THEN
      MatcherAssert.assertThat(
          "Expected error message is different than expected",
          errorBody.errors.email,
          Matchers.hasItemInArray("is invalid"));
    }
  }

  @Test
  @DisplayName("Register user with special characters or in strange format, check status code")
  void registerUserWithStrangeEmail() {

    for (RegistrationRequestUser user : testDataProvider.getUserWithStrangeEmail()) {
      //GIVEN
      prepareRequestBody(user);

      //WHEN
      responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);

      //THEN
      MatcherAssert.assertThat(
          "Expected username is different than actual",
          responseBody.user.username,
          Matchers.equalTo(requestBody.user.username));
    }
  }

  @Test
  @DisplayName("Register user with email which is already taken, check error message")
  void registerUserWithTakenEmailAddress() {
    // GIVEN
    requestBody = new RegistrationRequestUserDto(testDataProvider.getValidRegistrationUser());
    RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail())
        .body(requestBody)
        .post(USERS.getEndpoint());

    prepareRequestBody(testDataProvider.getValidRegistrationUser());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        errorBody.errors.email,
        Matchers.hasItemInArray("has already been taken"));
  }

  @Test
  @DisplayName("Register user with empty body, check error message")
  void registerUserWithEmptyBody() {
    // GIVEN
    prepareRequestBody(new RegistrationRequestUser());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        errorBody.errors.email,
        Matchers.hasItemInArray("can't be blank"));
  }

  private void prepareRequestBody(RegistrationRequestUser user) {
    requestBody = new RegistrationRequestUserDto(user);
    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);
  }

}
