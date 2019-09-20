package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.utils.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.utils.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.utils.StatusCode.CODE_200;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonswrappers.UserRequestWrapper;
import com.griddynamics.conduit.jsonswrappers.UserResponseWrapper;
import io.restassured.RestAssured;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationTest extends ApiTest {

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    // GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getPassword());
    UserRequestWrapper requestBody = new UserRequestWrapper(userBody);

    requestSpecification = RestAssured.given().contentType(APPLICATION_JSON).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS_LOGIN);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(TEST_DATA_PROVIDER.getUsername(), userResponseWrapper.user.username,
        "Username is different than expected");
  }

  @Test
  @DisplayName("Authentication - check if user with incorrect password can be log into app")
  void logUserWithIncorrrectPassword() {
    // GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getIncorrectPassword());
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification = RestAssured.given().contentType(APPLICATION_JSON).body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN);
    statusCode = response.statusCode();

    //THEN
    assertNotEquals(CODE_200, statusCode, "Actual status code is same as unexpected");

    MatcherAssert.assertThat(statusCode, Matchers.equalTo(CODE_200));
  }

}
