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
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Epic("Smoke tests")
@Feature("Authentication")
public class AuthenticationTest {
  private static RequestSpecification requestSpecification;
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest registeredUser = testDataProvider.getTestUser();

  private int statusCode;

  private UserRequest userBody;
  private UserRequestDto requestBody;


  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
    // todo: app stopped logging my randomly created user (security feature?!) so that's why
    //  I'm using registeredUser which is provided from testDataProvider
     //registerUser(registeredUser);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with valid credentials into app, check if ID is same as expected")
  @Test
  @DisplayName("Authenticate user with valid credentials, check ID")
  void authenticateValidUser() {
    // GIVEN
    prepareRequest(registeredUser.email, registeredUser.password);

    // WHEN
    UserResponseDto responseBody = makeApiCall();

    // THEN
    MatcherAssert.assertThat(
        "Actual username is different than expected",
        responseBody.user.username,
        Matchers.equalTo(registeredUser.username));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with incorrect password, check if response status code is 422")
  @Test
  @DisplayName("Authenticate user with incorrect password, check status code")
  void cantAuthenticateUserWithIncorrectPass() {
    // GIVEN
    prepareRequest(registeredUser.email, testDataProvider.getIncorrectPassword());

    // WHEN
    statusCode = getStatusFromApiCall();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(CODE_422.getValue()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with empty password, then check if status code is 422")
  @Test
  @DisplayName("Authenticate user with empty password, check status code")
  void cantAuthenticateUserWithEmptyPass() {
    // GIVEN
    prepareRequest(registeredUser.email, "");

    // WHEN
    statusCode = getStatusFromApiCall();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(CODE_422.getValue()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user without password, check if status code is 422")
  @Test
  @DisplayName("Authenticate user without password, check status code")
  void cantAuthenticateUserWithoutPass() {
    // GIVEN
    prepareRequest(registeredUser.email);

    // WHEN
    statusCode = getStatusFromApiCall();

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(CODE_422.getValue()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with empty body, check if error message is same as expected")
  @Test
  @DisplayName("Authenticate user with empty body, check error message")
  void checkErrorMessageForUserWithEmptyBody() {
    // GIVEN
    prepareRequest();

    // WHEN
    UnprocessableEntityErrorDto errorBody = getErrorBodyFromApiCall();

    // THEN
    MatcherAssert.assertThat(
        "Actual error message is different than expected",
        errorBody.errors.emailOrPassword,
        Matchers.hasItemInArray("is invalid"));
  }

  private static void registerUser(RegistrationRequestUser user) {
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    UserResponseDto responseBody =
        requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);
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
    return requestSpecification
        .post(USERS_LOGIN.getEndpoint())
        .as(UnprocessableEntityErrorDto.class);
  }

  private int getStatusFromApiCall() {
    return requestSpecification.post(USERS_LOGIN.getEndpoint()).statusCode();
  }

  private void prepareRequestSpecification() {
    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);
  }
}
