package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.PROFILES_USERNAME;
import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.USER;
import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.USERS_LOGIN;
import static com.griddynamics.conduit.test.utils.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.test.utils.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.test.utils.RequestSpecificationDetails.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.griddynamics.conduit.test.jsons.UserRequest;
import com.griddynamics.conduit.test.jsonswrappers.ProfileWrapper;
import com.griddynamics.conduit.test.jsonswrappers.UserRequestWrapper;
import com.griddynamics.conduit.test.jsonswrappers.UserResponseWrapper;
import com.griddynamics.conduit.test.utils.StatusCodes;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BasicTests extends ApiTest {

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    //GIVEN
    userBody = new UserRequest(DATA.getEmail(), DATA.getPassword());
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(DATA.getUsername(), userResponseWrapper.user.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Get User - get user, then check his bio and response status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN);

    //WHEN
    response = requestSpecification.get(USER);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    statusCode = response.statusCode();

    //THEN
    assertEquals(DATA.getBio(), userResponseWrapper.user.bio,
        "Expected user bio is different");
    Assertions.assertEquals(StatusCodes.CODE_200, statusCode,
        "Status code different than expected");
  }

  @Test
  @DisplayName("Get Profile - get user profile and check if username is the same as in query")
  void getProfileCheckUsername() {
    // GIVEN
    requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .pathParam(USERNAME, DATA.getUsername());

    //WHEN
    response = requestSpecification.get(PROFILES_USERNAME);
    responseBody = response.as(ProfileWrapper.class);

    //THEN
    Assertions.assertEquals(DATA.getUsername(), responseBody.profile.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Update User - update user bio and check if it was updated")
  void updateUserBioAndCheckIfUpdated() {
    //GIVEN
    userBody = new UserRequest(DATA.getUsername(), DATA.getEmail(), DATA.getUpdatedBio());
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification =
        RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .body(requestBody);

    //WHEN
    response = requestSpecification.put(USER);
    userResponseWrapper = response.as(UserResponseWrapper.class);

    //THEN
    assertEquals(DATA.getUpdatedBio(), userResponseWrapper.user.bio,
        "Expected user bio is different");
  }

  @Test
  @DisplayName("Delete Article - remove article")
  void deleteArticleBySlug() {
    //GIVEN

    //WHEN

    //THEN

  }
}
