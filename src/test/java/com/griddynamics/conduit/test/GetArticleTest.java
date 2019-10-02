package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Get Article")
public class GetArticleTest {
  // todo:
  //  5. add test with invalid slug
  //  6. add loggers

  private String slug;
  private static String token;
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest user = testDataProvider.getTestUser();

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    TokenProvider tokenProvider = new TokenProvider();
    token = tokenProvider.getTokenForUser(user);
  }

  @BeforeEach
  void prepareSlug() {
    Article article = new Article("Title", "Description", "Body");

    slug = getSlugFromCreatedArticle(article);
  }

  @AfterEach
  void removeArticle() {
    RequestSpecification requestSpecification = prepareRequestSpecification(token, slug);

    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    checkIfRemoved(statusCode != 200, "Article was not removed");
  }


  @Severity(SeverityLevel.NORMAL)
  @Description("Get already created article, check if slug match slug from path parameter")
  @Test
  @DisplayName("Get article article, check if slug is same as slug from path parameter")
  void getArticleCheckSlug() {
    // GIVEN
    RequestSpecification requestSpecification = prepareRequestSpecification(slug);

    // WHEN
    ArticleDto dto = requestSpecification.get(Endpoint.ARTICLES_SLUG.get()).as(ArticleDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Actual article slug is different than expected", dto.article.slug, Matchers.equalTo(slug));
  }

  private RequestSpecification prepareRequestSpecification(String slug) {
    return RestAssured.given().pathParam(SLUG.getDetails(), slug);
  }

  private RequestSpecification prepareRequestSpecification(String token, String slug) {
    return RestAssured.given()
        .header(AUTHORIZATION.getDetails(), token)
        .pathParam(SLUG.getDetails(), slug);
  }

  private static String getSlugFromCreatedArticle(Article article) {
    Response response = createArticle(article);
    ArticleDto createdArticle = response.as(ArticleDto.class);

    if (titlesNotEqual(article, createdArticle)) {
      throw new IllegalStateException(
          "Response article title is different than request article title ");
    }

    return createdArticle.article.slug;
  }

  private static Response createArticle(Article article) {
    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType(APPLICATION_JSON.getDetails())
            .header(AUTHORIZATION.getDetails(), token)
            .body(article);

    return requestSpecification.post(ARTICLES.get());
  }

  private static boolean titlesNotEqual(Article article, ArticleDto createdArticle) {
    return !createdArticle.article.title.equals(article.title);
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.delete(Endpoint.ARTICLES_SLUG.get()).statusCode();
  }

  private void checkIfRemoved(boolean isCode200, String message) {
    if (isCode200) {
      throw new IllegalStateException(message);
    }
  }
}
