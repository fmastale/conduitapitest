package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.UserRequest;
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
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest registeredUser = testDataProvider.getTestUserOne();

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with valid credentials into app, check if ID is same as expected")
  @Test
  @DisplayName("Authenticate user with valid credentials, check ID")
  void authenticateValidUser() {
    // GIVEN
    UserRequestDto requestBody =
        new UserRequestDto(
            new UserRequest(registeredUser.email, registeredUser.password));

    RequestSpecification requestSpecification = prepareRequestSpecification(requestBody);

    // WHEN
    UserResponseDto responseBody = makeApiCall(requestSpecification);

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
    UserRequestDto requestBody =
        new UserRequestDto(
            new UserRequest(registeredUser.email, testDataProvider.getIncorrectPassword()));

    RequestSpecification requestSpecification = prepareRequestSpecification(requestBody);

    // WHEN
    int statusCode = getStatusFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._422.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with empty password, then check if status code is 422")
  @Test
  @DisplayName("Authenticate user with empty password, check status code")
  void cantAuthenticateUserWithEmptyPass() {
    // GIVEN
    UserRequestDto requestBody =
        new UserRequestDto(
            new UserRequest(registeredUser.email, ""));

    RequestSpecification requestSpecification = prepareRequestSpecification(requestBody);

    // WHEN
    int statusCode = getStatusFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._422.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user without password, check if status code is 422")
  @Test
  @DisplayName("Authenticate user without password, check status code")
  void cantAuthenticateUserWithoutPass() {
    // GIVEN
    UserRequestDto requestBody =
        new UserRequestDto(
            new UserRequest(registeredUser.email));

    RequestSpecification requestSpecification = prepareRequestSpecification(requestBody);

    // WHEN
    int statusCode = getStatusFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._422.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Authenticate user with empty body, check if error message is same as expected")
  @Test
  @DisplayName("Authenticate user with empty body, check error message")
  void checkErrorMessageForUserWithEmptyBody() {
    // GIVEN
    UserRequestDto requestBody =
        new UserRequestDto(
            new UserRequest());
    
    RequestSpecification requestSpecification = prepareRequestSpecification(requestBody);

    // WHEN
    UnprocessableEntityErrorDto errorBody = getErrorBodyFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual error message is different than expected",
        errorBody.errors.emailOrPassword,
        Matchers.hasItemInArray("is invalid"));
  }

  private UserResponseDto makeApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.post(USERS_LOGIN.get()).as(UserResponseDto.class);
  }

  private UnprocessableEntityErrorDto getErrorBodyFromApiCall(
      RequestSpecification requestSpecification) {
    return requestSpecification.post(USERS_LOGIN.get()).as(UnprocessableEntityErrorDto.class);
  }

  private int getStatusFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.post(USERS_LOGIN.get()).statusCode();
  }

  private RequestSpecification prepareRequestSpecification(UserRequestDto requestBody) {
    return RestAssured.given().contentType(APPLICATION_JSON.get()).body(requestBody);
  }
}
