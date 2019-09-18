package basictestsv2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jsons.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wrappers.UserRequestWrapper;
import wrappers.UserResponseWrapper;

public class BasicTestsV2 {
  private static String URL = "https://conduit.productionready.io/api";

  @Test
  @DisplayName("Authentication - log user, check his ID and status code")
  void logUserCheckIdAndStatus() {
    //GIVEN
    RestAssured.baseURI = URL;
    UserRequestWrapper requestBodyWrapper = getUserRequestWrapper("adam@mail.com", "adam1234");

    RequestSpecification requestSpecification =
        RestAssured.given().contentType("application/json").body(requestBodyWrapper);

    //WHEN
    Response response = requestSpecification.post("/users/login");
    UserResponseWrapper responseBodyWrapper = response.as(UserResponseWrapper.class);

    //THEN
    assertEquals(responseBodyWrapper.getUserResponse().getBio(),
        "I like to skateboard all day",
        "Actual and expected user bio are not the same");

    assertEquals(response.statusCode(),
        200,
        "Actual and expected status code are not the same");
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
