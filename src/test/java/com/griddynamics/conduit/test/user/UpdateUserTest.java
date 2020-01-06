package com.griddynamics.conduit.test.user;

import static com.griddynamics.conduit.helpers.Endpoint.USER;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.UserRequest;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Update User")
public class UpdateUserTest extends BaseTest {

  private RequestSpecification requestSpecification;
  private UserResponseDto userResponse;

  @BeforeEach
  void setup() {
    prepareRandomBioAndImage(user);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Update user bio, check if actual bio is same as expected")
  @Test
  @DisplayName("Update user bio")
  void updateUserBio() {
    // GIVEN
    requestSpecification = prepareRequestSpecification(user);

    // WHEN
    userResponse = getUserFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual user bio is different than expected",
        userResponse.user.bio,
        Matchers.equalTo(user.bio));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Update user with using additional fields, check if actual bio and image are same as expected")
  @Test
  @DisplayName("Update user with using additional fields")
  void updateUserWithUsingAdditionalFields() {
    // GIVEN
    requestSpecification = prepareRequestSpecification(user);

    // WHEN
    userResponse = getUserFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual user bio is different than expected",
        userResponse.user.bio,
        Matchers.equalTo(user.bio));
    MatcherAssert.assertThat(
        "Actual user image is different than expected",
        userResponse.user.image,
        Matchers.equalTo(user.image));
  }

  private void prepareRandomBioAndImage(UserRequest user) {
    user.bio = testDataProvider.getRandomBio();
    user.image = testDataProvider.getRandomImg();
  }

  private RequestSpecification prepareRequestSpecification(UserRequest user) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.get())
        .header(AUTHORIZATION.get(), token)
        .body(user);
  }

  private UserResponseDto getUserFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.put(USER.get()).as(UserResponseDto.class);
  }
}
