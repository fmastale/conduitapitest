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

  //USERNAME
  @Test
  @DisplayName("Register user with all fields valid, check if username match")
  void registerUserWithValidData() {

    for (RegistrationRequestUser user : testDataProvider.getValidUsers()) {
      // GIVEN
      prepareRequestBody(user);

      // WHEN
      responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);

      // THEN
      MatcherAssert.assertThat(
          "Expected username is different than actual",
          responseBody.user.username,
          Matchers.equalTo(user.username));
    }
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

  @Test
  @DisplayName("Register user without specifying username, check error message")
  void registerUserWithoutUsername() {
    // GIVEN
    prepareRequestBody(testDataProvider.getUserWithNullName());

    // WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    // THEN
    // todo: in response I've got error connected to too long username - because it is taking
    //  String full name and count it's letters
    MatcherAssert.assertThat(
        "Expected error messages are different than actual",
        errorBody.errors.username,
        Matchers.hasItemInArray("is too long (maximum is 20 characters)"));
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


  //todo: empty
  //EMAIL
  // I believe there is no such thing as too long email - I put 1800 chars into it and register user
  // minimal email format is '*@*.*' but most of them already used, so it will be hard to generate
  @Test
  @DisplayName("Register user with in strange format, check status code")
  void registerUserWithStrangeEmail() {
    //todo: re-work this method and it's data provider
    for (RegistrationRequestUser user : testDataProvider.getUsersWithStrangeEmailFormat()) {
      //GIVEN
      prepareRequestBody(user);

      //WHEN
      responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);

      //THEN
      MatcherAssert.assertThat(
          "Expected username is different than actual",
          responseBody.user.username,
          Matchers.equalTo(user.username));
    }
  }

  @Test
  @DisplayName("Register user with special characters in email, check status code")
  void registerUserWithSpecialCharsInEmail() {
    //GIVEN
    // todo: change method so it will be returning requestBody?!
    // todo:  stable: '!#$%^&@mail.com' vs random: '攫攫攫攫@mail.com'
    prepareRequestBody(testDataProvider.getUserWithSpecialCharsEmail());

    // WHEN
    responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);

    //THEN
    MatcherAssert.assertThat(
        "Expected username is different than actual",
        responseBody.user.username,
        Matchers.equalTo(requestBody.user.username));

  }

  @Test
  @DisplayName("Register user with space inside email, check error message")
  void registerUserWithSpaceInEmail() {
    //GIVEN
    prepareRequestBody(testDataProvider.getUserWithSpaceInEmail());

    //WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    //THEN
    MatcherAssert.assertThat(
        "Expected error message is different than expected",
        errorBody.errors.email,
        Matchers.hasItemInArray("is invalid"));

  }

  @Test
  @DisplayName("Register user with email which is incorrectly formatted, check error message")
  void registerUserWithIncorrectlyFormattedEmail() {

    for (RegistrationRequestUser user : testDataProvider.getUsersWithWrongEmailFormat()) {
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
  @DisplayName("Register user with empty email in request body, check error message")
  void registerUserWithEmptyEmail() {
    //GIVEN
    prepareRequestBody(testDataProvider.getUserWithEmptyEmail());

    //WHEN
    errorBody =
        requestSpecification.post(USERS.getEndpoint()).as(UnprocessableEntityErrorDto.class);

    //THEN
    MatcherAssert.assertThat(
        "Expected error message is different than expected",
        errorBody.errors.email,
        Matchers.hasItemInArray("can't be blank"));
  }

  // todo: password max/min length, special chars, space inside, empty
  //PASSWORD
  @Test
  @DisplayName("Check password")
  void registerUserWithPassword() {
    //GIVEN

    //WHEN

    //THEN

  }


  //GENERAL
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
