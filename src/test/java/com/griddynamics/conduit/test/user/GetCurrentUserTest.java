package com.griddynamics.conduit.test.user;

import static com.griddynamics.conduit.helpers.Endpoint.USER;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.UserRequest;
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
@Feature("Get Current User")
public class GetCurrentUserTest {

  private static String token;
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest user = testDataProvider.getTestUserOne();

  private RequestSpecification requestSpecification;
  private String username = testDataProvider.getTestUserUsername();

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    token = new TokenProvider().getTokenForUser(user);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get current user by using correct token, check if username is same as expected")
  @Test
  @DisplayName("Get current user by using correct token, check username")
  void getUserWithCorrectToken() {
    // GIVEN
    requestSpecification = prepareRequestSpecification(token);

    // WHEN
    UserResponseDto response = getUserFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual username is different than expected",
        response.user.username,
        Matchers.equalTo(username));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get current user by using incorrect token, check if status code is 401")
  @Test
  @DisplayName("Get current user by using incorrect token, check status code")
  void cantGetUserWithIncorrectToken() {
    // GIVEN
    String incorrectToken = token + "AAA";
    requestSpecification = prepareRequestSpecification(incorrectToken);

    // WHEN
    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._401.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get current user without using andy token, check if status code is 401")
  @Test
  @DisplayName("Get current user without specifying token, check status code")
  void cantGetUserWithoutToken() {
    // GIVEN
    requestSpecification = prepareRequestSpecification();

    // WHEN
    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._401.get()));
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(USER.get()).statusCode();
  }

  private UserResponseDto getUserFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(USER.get()).as(UserResponseDto.class);
  }

  private RequestSpecification prepareRequestSpecification(String token) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.get())
        .header(AUTHORIZATION.get(), token);
  }

  private RequestSpecification prepareRequestSpecification() {
    return RestAssured.given().contentType(APPLICATION_JSON.get());
  }
}
