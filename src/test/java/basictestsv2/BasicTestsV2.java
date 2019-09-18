package basictestsv2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jsons.ProfileResponse;
import jsons.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wrappers.ProfileResponseWrapper;
import wrappers.UserRequestWrapper;
import wrappers.UserResponseWrapper;

public class BasicTestsV2 {
  private static String URL = "https://conduit.productionready.io/api";
  private static String TOKEN =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NjY1MTIsInVzZXJuYW1lIjoiYWRhbTEyMzRpbyIsImV4cCI6MTU3Mzk5MjY5OX0.OVz0_RrrodT80ki_y5yWqg0gLUoMrGuO_WH4R-kg4Tc";

  @Test
  @DisplayName("Authentication - log user, check his ID and status code")
  void logUserCheckIdAndStatus() {
    // GIVEN
    RestAssured.baseURI = URL;
    UserRequestWrapper requestBodyWrapper = getUserRequestWrapper("adam@mail.com", "adam1234");

    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType("application/json")
            .body(requestBodyWrapper)
            .log()
            .all();

    // WHEN
    Response response = requestSpecification.post("/users/login");
    response.prettyPrint();

    UserResponseWrapper responseBodyWrapper = response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(
        66512,
        responseBodyWrapper
            .getUserResponse()
            .getId(),
        "Actual and expected user ID are not the same");

    assertEquals(200, response.statusCode(), "Actual and expected status code are not the same");
  }

  @Test
  @DisplayName("Get current User - check user email and status code")
  void getUserCheckEmailAndStatusCode() {
    // GIVEN
    RestAssured.baseURI = URL;
    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType("application/json")
            .header("Authorization", "Token " + TOKEN);

    // WHEN
    Response response = requestSpecification.get("/user");
    UserResponseWrapper responseBodyWrapper = response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(
        "adam@mail.com",
        responseBodyWrapper
            .getUserResponse()
            .getEmail(),
        "Expected and actual not same");
  }
  
  @Test
  @DisplayName("Get Profile - check username and status code")
  void getProfileCheckUsernameAndStatus() {
    //GIVEN
    RestAssured.baseURI = URL;
    RequestSpecification requestSpecification =
        RestAssured.given()
            .pathParam("username", "adam1234io");

    // WHEN
    Response response = requestSpecification.get("/profiles/{username}");
    ProfileResponseWrapper profileResponseBody = response.as(ProfileResponseWrapper.class);

    //THEN
    assertEquals("adam1234io",
        profileResponseBody.getProfileResponse().getUsername(),
        "not same");

      
  }



  private UserRequestWrapper getUserRequestWrapper(String email, String password) {
    UserRequest userRequestBody = new UserRequest();
    userRequestBody.setEmail(email);
    userRequestBody.setPassword(password);

    UserRequestWrapper requestBodyWrapper = new UserRequestWrapper();
    requestBodyWrapper.setUserRequest(userRequestBody);
    return requestBodyWrapper;
  }
}
