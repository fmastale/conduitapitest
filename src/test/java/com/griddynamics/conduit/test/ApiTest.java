package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.test.utils.ApiAddressesUtil.URI;
import static io.restassured.RestAssured.*;

import com.griddynamics.conduit.test.jsons.UserRequest;
import com.griddynamics.conduit.test.jsonswrappers.ProfileWrapper;
import com.griddynamics.conduit.test.jsonswrappers.UserRequestWrapper;
import com.griddynamics.conduit.test.jsonswrappers.UserResponseWrapper;
import com.griddynamics.conduit.test.utils.ApiAddressesUtil;
import com.griddynamics.conduit.test.utils.RequestSpecificationDetails;
import com.griddynamics.conduit.test.utils.TestDataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiTest {
  protected static String TOKEN;
  protected static TestDataProvider DATA;

  protected int statusCode;

  protected Response response;
  protected UserRequest userBody;
  protected ProfileWrapper responseBody;
  protected UserRequestWrapper requestBody;
  protected UserResponseWrapper userResponseWrapper;
  protected RequestSpecification requestSpecification;

  public ApiTest() {
    baseURI = URI;

    DATA = new TestDataProvider();

    TOKEN = getToken(DATA.getEmail(), DATA.getPassword());
  }

  public UserResponseWrapper logUser(String email, String password) {
    userBody = new UserRequest(email, password);
    requestBody = new UserRequestWrapper(userBody);

    requestSpecification =
        RestAssured.given().contentType(RequestSpecificationDetails.APPLICATION_JSON).body(requestBody);

    userResponseWrapper =
        requestSpecification.post(ApiAddressesUtil.USERS_LOGIN).as(UserResponseWrapper.class);

    return userResponseWrapper;
  }

  public String getToken(String email, String password) {
    this.userResponseWrapper = logUser(email, password);
    return "Token " + userResponseWrapper.user.token;
  }
}
