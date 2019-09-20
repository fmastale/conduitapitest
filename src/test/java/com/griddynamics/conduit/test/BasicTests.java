package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.utils.Endpoint.ARTICLES_SLUG;
import static com.griddynamics.conduit.utils.Endpoint.PROFILES_USERNAME;
import static com.griddynamics.conduit.utils.Endpoint.USER;
import static com.griddynamics.conduit.utils.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.utils.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.utils.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.utils.RequestSpecificationDetails.SLUG;
import static com.griddynamics.conduit.utils.RequestSpecificationDetails.USERNAME;
import static com.griddynamics.conduit.utils.StatusCode.CODE_200;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonswrappers.ProfileWrapper;
import com.griddynamics.conduit.jsonswrappers.UserRequestWrapper;
import com.griddynamics.conduit.jsonswrappers.UserResponseWrapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BasicTests extends ApiTest {

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    //GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getPassword());
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(TEST_DATA_PROVIDER.getUsername(), userResponseWrapper.user.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Get User - get user, then check his bio and response status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN);

    //WHEN
    response = requestSpecification.get(USER);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    statusCode = response.statusCode();

    //THEN
    assertEquals(TEST_DATA_PROVIDER.getBio(), userResponseWrapper.user.bio,
        "Expected user bio is different");
    Assertions.assertEquals(CODE_200, statusCode,
        "Status code different than expected");
  }

  @Test
  @DisplayName("Get Profile - get user profile and check if username is the same as in query")
  void getProfileCheckUsername() {
    // GIVEN
    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .pathParam(USERNAME, TEST_DATA_PROVIDER.getUsername());

    //WHEN
    response = requestSpecification.get(PROFILES_USERNAME);
    responseBody = response.as(ProfileWrapper.class);

    //THEN
    Assertions.assertEquals(TEST_DATA_PROVIDER.getUsername(), responseBody.profile.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Update User - update user bio and check if it was updated")
  void updateUserBioAndCheckIfUpdated() {
    //GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getUsername(), TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER
        .getUpdatedBio());
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .body(requestBody);

    //WHEN
    response = requestSpecification.put(USER);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    //THEN
    assertEquals(TEST_DATA_PROVIDER.getUpdatedBio(), userResponseWrapper.user.bio,
        "Expected user bio is different");
  }

  @Test
  @DisplayName("Delete Article - remove article")
  void deleteArticleBySlug() {
    //GIVEN
    String slug = "how-to-automate-test-in-restassured-cshiu1";

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .pathParam(SLUG, slug);

    //WHEN
    response = requestSpecification.delete(ARTICLES_SLUG);
    statusCode = response.statusCode();

    //THEN
    assertEquals(CODE_200, statusCode, "Status code is different than expected");

  }
}
