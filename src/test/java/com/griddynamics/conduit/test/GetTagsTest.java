package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Tags;
import com.griddynamics.conduit.jsons.UserRequest;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Get Tags")
public class GetTagsTest {
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest user = testDataProvider.getTestUserOne();

  // if I add tag to article it isn't shown in the tag list, so probably list contains only those which are most popular
  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    String token = new TokenProvider().getTokenForUser(user);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get list of tags, check if list contains 20 elements")
  @Test
  @DisplayName("Get tags list, check length")
  void getTagsCheckLength() {
    // GIVEN
    RequestSpecification requestSpecification = RestAssured.given();

    // WHEN
    Tags tagsList = requestSpecification.get(Endpoint.TAGS.get()).as(Tags.class);

    // THEN
    MatcherAssert.assertThat(
        "Actual tags list length is different than expected",
        tagsList.tags.length,
        Matchers.equalTo(20));
  }
}
