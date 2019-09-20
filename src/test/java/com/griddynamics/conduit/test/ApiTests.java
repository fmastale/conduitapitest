package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.PROFILES_USERNAME;
import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.URI;
import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.USER;
import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.USERS_LOGIN;
import static com.griddynamics.conduit.test.utils.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.test.utils.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.test.utils.RequestSpecificationDetails.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.griddynamics.conduit.test.jsons.UserRequest;
import com.griddynamics.conduit.test.jsonswrappers.ProfileWrapper;
import com.griddynamics.conduit.test.jsonswrappers.UserRequestWrapper;
import com.griddynamics.conduit.test.utils.StatusCodes;
import com.griddynamics.conduit.test.jsonswrappers.UserResponseWrapper;
import com.griddynamics.conduit.test.utils.TestDataProvider;
import com.griddynamics.conduit.test.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiTests {
  private static String TOKEN;
  private static TestDataProvider DATA = new TestDataProvider();

  @BeforeAll
  @DisplayName("Environment setup - getting valid token and setting base URI")
  static void envSetup() {
    RestAssured.baseURI = URI;

    TestUtils testUtil = new TestUtils();
    TOKEN = "Token " + testUtil.getToken(DATA.getEmail(), DATA.getPassword());
  }

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    //GIVEN
    UserRequest userBody = new UserRequest(DATA.getEmail(), DATA.getPassword());
    UserRequestWrapper requestBody = new UserRequestWrapper(userBody);

    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .body(requestBody);

    //WHEN
    Response response = requestSpecification.post(USERS_LOGIN);
    UserResponseWrapper responseBody= response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(DATA.getUsername(), responseBody.user.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Get User - get user, then check his bio and response status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN);

    //WHEN
    Response response = requestSpecification.get(USER);
    UserResponseWrapper responseBody = response.as(UserResponseWrapper.class);

    int statusCode = response.statusCode();

    //THEN
    assertEquals(DATA.getBio(), responseBody.user.bio,
        "Expected user bio is different");
    Assertions.assertEquals(StatusCodes.CODE_200, statusCode,
        "Status code different than expected");
  }

  @Test
  @DisplayName("Get Profile - get user profile and check if username is the same as in query")
  void getProfileCheckUsername() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .pathParam(USERNAME, DATA.getUsername());

    //WHEN
    Response response = requestSpecification.get(PROFILES_USERNAME);
    ProfileWrapper responseBody = response.as(ProfileWrapper.class);

    //THEN
    Assertions.assertEquals(DATA.getUsername(), responseBody.profile.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Update User - update user bio and check if it was updated")
  void updateUserBioAndCheckIfUpdated() {
    //GIVEN
    UserRequest userBody = new UserRequest(DATA.getUsername(), DATA.getEmail(), DATA.getUpdatedBio());
    UserRequestWrapper requestBody = new UserRequestWrapper(userBody);

    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .body(requestBody);

    //WHEN
    Response response = requestSpecification.put(USER);
    UserResponseWrapper responseBody = response.as(UserResponseWrapper.class);

    //THEN
    assertEquals(DATA.getUpdatedBio(), responseBody.user.bio,
        "Expected user bio is different");
  }
}
