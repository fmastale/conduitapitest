package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES_LIMIT;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.LIMIT_NUMBER;
import static com.griddynamics.conduit.helpers.StatusCode.CODE_200;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.jsonsdtos.ArticlesDto;
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
@Feature("List Articles")
public class ListArticlesTest {

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get list of articles, check if status code is equal to 200")
  @Test
  @DisplayName("Get list of articles, check status code")
  void getListArticlesCheckStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification = RestAssured.given();

    // WHEN
    int statusCode = requestSpecification.get(ARTICLES.getEndpoint()).statusCode();

    // THEN
    // todo: better way of validating list of articles?!
    MatcherAssert.assertThat("", statusCode, Matchers.equalTo(CODE_200.getValue()));
  }

  @Test
  @DisplayName("Get list of articles limited to 5, check if list size is less or equal 5")
  void getLimitedListOfArticles() {
    // GIVEN
    int number = 5;
    RequestSpecification requestSpecification = RestAssured.given().pathParam(LIMIT_NUMBER.getDetails(), number );

    // WHEN
    ArticlesDto articles = requestSpecification.get(ARTICLES_LIMIT.getEndpoint()).as(ArticlesDto.class);

    // THEN
    MatcherAssert.assertThat("", articles.articles.size() <= number, Matchers.equalTo(true));
  }
}
