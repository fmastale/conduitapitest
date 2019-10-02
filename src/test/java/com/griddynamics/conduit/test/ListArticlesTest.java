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
  private RequestSpecification requestSpecification;

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get list of articles, check if status code is equal to 200")
  @Test
  @DisplayName("Get list of articles, check status code")
  void getListOfArticlesCheckStatusCode() {
    // GIVEN
    requestSpecification = RestAssured.given();

    // WHEN
    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    // todo: better way of validating list of articles?!
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(CODE_200.getValue()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get list of articles limited to 5, check if list size is less or equal 5")
  @Test
  @DisplayName("Get limited list of articles, check size")
  void getLimitedListOfArticlesCheckSize() {
    // GIVEN
    int number = 5;
    requestSpecification = prepareRequestSpecification(LIMIT_NUMBER.getDetails(), number);

    // WHEN
    ArticlesDto articles = getListArticlesFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual list of articles is greater than expected",
        articles.articles.size() <= number,
        Matchers.equalTo(true));
  }

  private ArticlesDto getListArticlesFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(ARTICLES_LIMIT.get()).as(ArticlesDto.class);
  }

  private RequestSpecification prepareRequestSpecification(String path, int number) {
    return RestAssured.given().pathParam(path, number);
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(ARTICLES.get()).statusCode();
  }
}
