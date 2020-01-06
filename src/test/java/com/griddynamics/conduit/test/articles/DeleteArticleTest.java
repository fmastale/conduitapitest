package com.griddynamics.conduit.test.articles;

import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

import com.griddynamics.conduit.helpers.ArticleHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.UserRequest;
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
@Feature("Delete Article")
public class DeleteArticleTest extends BaseTest {
  private String slug;
  private ArticleHelper articleHelper = new ArticleHelper(token);

  @BeforeEach
  void setup() {
    slug = articleHelper.getSlugFromCreatedArticle();
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Delete article, check if status code is 200")
  @Test
  @DisplayName("Delete article, check status code")
  void deleteArticleCheckStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification = prepareRequestSpecification(token, slug);

    // WHEN
    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._200.get()));
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.delete(Endpoint.ARTICLES_SLUG.get()).statusCode();
  }

  private RequestSpecification prepareRequestSpecification(String token, String slug) {
    return RestAssured.given().header(AUTHORIZATION.get(), token).pathParam(SLUG.get(), slug);
  }
}
