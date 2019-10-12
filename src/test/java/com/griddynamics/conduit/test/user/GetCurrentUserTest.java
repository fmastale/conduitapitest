package com.griddynamics.conduit.test.user;

import com.griddynamics.conduit.helpers.StatusCode;
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

import static com.griddynamics.conduit.helpers.Endpoint.USER;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;

@Epic("Smoke tests")
@Feature("Get Current User")
public class GetCurrentUserTest extends BaseTest {

  private RequestSpecification requestSpecification;
  private String username = testDataProvider.getTestUserUsername();

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
