package com.griddynamics.conduit.test;


import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.StatusCode.CODE_422;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UnprocessableEntityErrorDto;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationTest {
  private static RequestSpecification requestSpecification;
  private static RegistrationRequestUser user = new TestDataProvider().getValidRegistrationUser();

  private int statusCode;

  private UserRequest userBody;
  private UserRequestDto requestBody;
  private TestDataProvider testDataProvider = new TestDataProvider();

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
    registerUser(user);
  }

  @Test
  @DisplayName("Log user with valid credentials, check if ID match")
  void logUserAndGetHisId() {
    // GIVEN
    prepareRequest(user.email, user.password);

    // WHEN
    UserResponseDto responseBody = makeApiCall();

    // THEN
    MatcherAssert.assertThat("Username is different than expected",
        responseBody.user.username, Matchers.equalTo(user.username));
  }

  @Test
  @DisplayName("Log user with incorrect password, check status code equals to 422")
  void logUserWithIncorrectPassword() {
    // GIVEN
    prepareRequest(user.email, testDataProvider.getIncorrectPassword());

    //WHEN
    statusCode = getStatusFromApiCall();

    //THEN
    MatcherAssert.assertThat("Actual status code is not the one we expected",
        statusCode, Matchers.equalTo(CODE_422.getValue()));
  }

  @Test
  @DisplayName("Log user with correct email and empty password, check status code equals to 422")
  void logUserWithEmptyPassword() {
    // GIVEN
    prepareRequest(user.email, "");

    // WHEN
    statusCode = getStatusFromApiCall();

    //THEN
    MatcherAssert.assertThat("Expected status code is different than actual",
        statusCode, Matchers.equalTo(CODE_422.getValue()));
  }

  @Test
  @DisplayName("Log user with correct email and empty password, check status code equals to 422")
  void logUserWithoutPassword() {
    // GIVEN
    prepareRequest(user.email);

    // WHEN
    statusCode = getStatusFromApiCall();

    //THEN
    MatcherAssert.assertThat("Expected status code is different than actual",
        statusCode, Matchers.equalTo(CODE_422.getValue()));
  }

  @Test
  @DisplayName("Log user with empty body, check error message")
  void checkErrorMessageForUserWithEmptyBody() {
    // GIVEN
    prepareRequest();

    // WHEN
    UnprocessableEntityErrorDto errorBody = getErrorBodyFromApiCall();

    //THEN
    MatcherAssert.assertThat("Expected error message is different than actual",
        errorBody.errors.emailOrPassword, Matchers.hasItemInArray("is invalid"));
  }


  private static void registerUser(RegistrationRequestUser user) {
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    UserResponseDto responseBody = requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);
  }


  private void prepareRequest(String email, String password) {
    prepareRequestBody(email, password);
    prepareRequestSpecification();
  }

  private void prepareRequest(String email) {
    prepareRequestBody(email);
    prepareRequestSpecification();
  }

  private void prepareRequest() {
    prepareRequestBody();
    prepareRequestSpecification();
  }

  private UserResponseDto makeApiCall() {
    return requestSpecification.post(USERS_LOGIN.getEndpoint()).as(UserResponseDto.class);
  }


  private void prepareRequestBody(String email, String password) {
    userBody = new UserRequest(email, password);
    requestBody = new UserRequestDto(userBody);
  }

  private void prepareRequestBody(String email) {
    userBody = new UserRequest(email);
    requestBody = new UserRequestDto(userBody);
  }

  private void prepareRequestBody() {
    userBody = new UserRequest();
    requestBody = new UserRequestDto(userBody);
  }


  private UnprocessableEntityErrorDto getErrorBodyFromApiCall() {
    return requestSpecification.post(USERS_LOGIN.getEndpoint())
            .as(UnprocessableEntityErrorDto.class);
  }

  private int getStatusFromApiCall() {
    return requestSpecification.post(USERS_LOGIN.getEndpoint()).statusCode();
  }

  private void prepareRequestSpecification() {
    requestSpecification = RestAssured.given()
            .contentType(APPLICATION_JSON.getDetail()).body(requestBody);
  }
}

