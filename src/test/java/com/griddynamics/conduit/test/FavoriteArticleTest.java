package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

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

@Epic("Smoke tests")
@Feature("Favorite Article Test")
public class FavoriteArticleTest {
  private static String authorsToken;
  private static String followerToken;
  private String slug;

  @BeforeAll
  static void prepareRequest() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    authorsToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserOne());
    followerToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserTwo());
  }

  @BeforeEach
  void prepareSlug() {
    Article article = new Article("Title", "Description", "Body");
    slug = getSlugFromCreatedArticle(article, authorsToken);
  }

  @AfterEach
  void cleanup() {
    removeArticle(slug, authorsToken);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Favorite article, check if field 'favorited' is set to true")
  @Test
  @DisplayName("Favorite article, check if favorited")
  void favoriteArticleCheckFavorited() {
    // GIVEN
    RequestSpecification requestSpecification = prepareRequestSpecification(slug, followerToken);

    // WHEN
    ArticleDto response = getArticleFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat("", response.article.favorited, Matchers.equalTo(true));
  }


  private ArticleDto getArticleFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.post(Endpoint.ARTICLES_SLUG_FAVORITE.get()).as(ArticleDto.class);
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
    RequestSpecification requestSpecification = RestAssured.given()
        .header(AUTHORIZATION.get(), token)
        .pathParam(SLUG.get(), slug);

    int statusCode = requestSpecification.delete(Endpoint.ARTICLES_SLUG.get()).statusCode();

    if (statusCode != 200) {
      throw new IllegalStateException("Article wasn't removed");
    }
  }
}
