package com.griddynamics.conduit.test.articles;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES_LIMIT;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.LIMIT_NUMBER;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.jsonsdtos.ArticlesListDto;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("List Articles")
public class ListArticlesTest extends BaseTest {

  @Severity(SeverityLevel.NORMAL)
  @Description("Get list of articles, check if status code is equal to 200")
  @Test
  @DisplayName("Get list of articles, check status code")
  void getListOfArticlesCheckStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification = RestAssured.given();

    // WHEN
    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get list of articles limited to 5, check if list size is less or equal 5")
  @Test
  @DisplayName("Get limited list of articles, check size")
  void getLimitedListOfArticlesCheckSize() {
    // GIVEN
    int number = 5;
    RequestSpecification requestSpecification =
        prepareRequestSpecification(LIMIT_NUMBER.get(), number);

    // WHEN
    ArticlesListDto articles = getListArticlesFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual list of articles is greater than expected",
        articles.articles.size() <= number,
        Matchers.equalTo(true));
  }

  private ArticlesListDto getListArticlesFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(ARTICLES_LIMIT.get()).as(ArticlesListDto.class);
  }

  private RequestSpecification prepareRequestSpecification(String path, int number) {
    return RestAssured.given().pathParam(path, number);
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(ARTICLES.get()).statusCode();
  }
}
