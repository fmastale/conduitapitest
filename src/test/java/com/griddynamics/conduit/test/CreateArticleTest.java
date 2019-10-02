package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.StatusCode._200;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Article;
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

// todo: add creating article without authentication, create article without all needed fields

@Epic("Smoke tests")
@Feature("Create Articles Test")
public class CreateArticleTest {
  private static String token;
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest user = testDataProvider.getTestUser();

  private int statusCode;
  private Article article;
  private RequestSpecification requestSpecification;

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    TokenProvider tokenProvider = new TokenProvider();
    token = tokenProvider.getTokenForUser(user);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Create article, check if status code is 200")
  @Test
  @DisplayName("Create article, check status code")
  void createArticle() {
    // GIVEN
    article = prepareArticle();

    requestSpecification = prepareRequestSpecification(article, token);

    // WHEN
    statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(_200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Create article with additional field, check if status code is 200")
  @Test
  @DisplayName("Create article with additional field, check status code")
  void createArticleWithAdditionalFieldCheckStatus() {
    // GIVEN
    String[] tags = {"tag1", "tag2"};
    article = prepareArticle(tags);

    requestSpecification = prepareRequestSpecification(article, token);

    // WHEN
    statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(_200.get()));
  }

  private RequestSpecification prepareRequestSpecification(Article article, String token) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.getDetails())
        .header(AUTHORIZATION.getDetails(), token)
        .body(article);
  }

  private Article prepareArticle(String[] tags) {
    return new Article("This is article title", "This is description", "This is body", tags);
  }

  private Article prepareArticle() {
    return new Article("This is article title", "This is description", "This is body");
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.post(ARTICLES.get()).statusCode();
  }
}
