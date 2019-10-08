package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.RequestSpecificationDetails;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Article;
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

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.*;

public class UnfavoriteArticleTest {
  private static String authorsToken;
  private static String followerToken;
  private String slug;

  @Epic("Smoke tests")
  @Feature("Unfavorite Article")
  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    authorsToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserOne());
    followerToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserTwo());
  }

  @BeforeEach
  void prepareArticleSlugAndFavorite() {
    Article article = new Article("Title", "Description", "Body");
    slug = getSlugFromCreatedArticle(article, authorsToken);

    // Favorite article
    RequestSpecification requestSpecification = prepareRequestSpecification(slug, followerToken);
    ArticleDto response = getArticleFromApiCall(requestSpecification);
  }

  @AfterEach
  void cleanup() {
    removeArticle(slug, authorsToken);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Unfavorite article, check if field 'favorited' is set to false")
  @Test
  @DisplayName("Unfavorite article, check if unfavorited")
  void favoriteArticleCheckFavorited() {
    // GIVEN
    RequestSpecification requestSpecification = prepareRequestSpecification(slug, followerToken);

    // WHEN
    ArticleDto response = checkArticleFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat("", response.article.favorited, Matchers.equalTo(false));
  }

  private ArticleDto getArticleFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.post(Endpoint.ARTICLES_SLUG_FAVORITE.get()).as(ArticleDto.class);
  }

  private ArticleDto checkArticleFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.delete(Endpoint.ARTICLES_SLUG_FAVORITE.get()).as(ArticleDto.class);
  }

  private RequestSpecification prepareRequestSpecification(String slug, String token) {
    return RestAssured.given()
        .header(RequestSpecificationDetails.AUTHORIZATION.get(), token)
        .pathParam(RequestSpecificationDetails.SLUG.get(), slug);
  }

  private static String getSlugFromCreatedArticle(Article article, String token) {
    Response response = createArticle(article, token);
    ArticleDto createdArticle = response.as(ArticleDto.class);

    if (titlesNotEqual(article, createdArticle)) {
      throw new IllegalStateException(
          "Response article title is different than request article title ");
    }
    return createdArticle.article.slug;
  }

  private static Response createArticle(Article article, String token) {
    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType(APPLICATION_JSON.get())
            .header(AUTHORIZATION.get(), token)
            .body(article);

    return requestSpecification.post(ARTICLES.get());
  }

  private static boolean titlesNotEqual(Article article, ArticleDto createdArticle) {
    return !createdArticle.article.title.equals(article.title);
  }

  private void removeArticle(String slug, String token) {
    RequestSpecification requestSpecification =
        RestAssured.given().header(AUTHORIZATION.get(), token).pathParam(SLUG.get(), slug);

    int statusCode = requestSpecification.delete(Endpoint.ARTICLES_SLUG.get()).statusCode();

    if (statusCode != 200) {
      throw new IllegalStateException("Article wasn't removed");
    }
  }
}
