package v2tests.testutils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import v2tests.jsons.UserRequest;
import v2tests.jsonwrappers.UserRequestWrapper;
import v2tests.jsonwrappers.UserResponseWrapper;

public class TestUtils {
  //todo: move uri to properties file
  private static final String URI = "https://conduit.productionready.io/api";
  private UserResponseWrapper response;

  private final String email = "adam@mail.com";
  private final String password = "adam1234";

  public UserResponseWrapper loginUser(String email, String password) {
    RestAssured.baseURI = URI;
    UserRequest userRequest = new UserRequest(email, password);
    UserRequestWrapper requestBody = new UserRequestWrapper(userRequest);

    RequestSpecification requestSpecification =
        RestAssured.given().contentType("application/json").body(requestBody);

    UserResponseWrapper response =
        requestSpecification.post("/users/login").as(UserResponseWrapper.class);

    return response;
  }

  public String getToken() {
    this.response = loginUser(email, password);
    return response.user.token;
  }
}
