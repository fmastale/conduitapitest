package com.griddynamics.conduit.test;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonswrappers.ProfileWrapper;
import com.griddynamics.conduit.jsonswrappers.UserRequestWrapper;
import com.griddynamics.conduit.jsonswrappers.UserResponseWrapper;
import com.griddynamics.conduit.utils.Endpoint;
import com.griddynamics.conduit.utils.RequestSpecificationDetails;
import com.griddynamics.conduit.utils.TestDataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiTest {
  protected static String TOKEN;
  protected static final TestDataProvider TEST_DATA_PROVIDER = new TestDataProvider();

  protected int statusCode;

  protected Response response;
  protected UserRequest userBody;
  protected ProfileWrapper responseBody;
  protected UserRequestWrapper requestBody;
  protected UserResponseWrapper userResponseWrapper;
  protected RequestSpecification requestSpecification;

  public ApiTest() {
    RestAssured.baseURI = Endpoint.BASE_URI;

    TOKEN = getToken(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getPassword());
  }

  protected UserResponseWrapper logUser(String email, String password) {
    userBody = new UserRequest(email, password);
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification =
        RestAssured.given().contentType(RequestSpecificationDetails.APPLICATION_JSON).body(requestBody);

    userResponseWrapper =
        requestSpecification.post(Endpoint.USERS_LOGIN).as(UserResponseWrapper.class);

    return userResponseWrapper;
  }

  private String getToken(String email, String password) {
    this.userResponseWrapper = logUser(email, password);
    return "Token " + userResponseWrapper.user.token;
  }
}
