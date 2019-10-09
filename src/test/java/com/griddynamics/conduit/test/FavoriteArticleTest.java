package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.ArticleHelper;
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

  private String articleId;
  private ArticleHelper articleHelper = new ArticleHelper();

  @BeforeAll
  static void prepareRequest() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    authorsToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserOne());
    followerToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserTwo());
  }

  @BeforeEach
  void prepareSlug() {
    Article article = new Article("Title", "Description", "Body");
    articleId = articleHelper.getSlugFromCreatedArticle(authorsToken);
  }

  @AfterEach
  void cleanup() {
    articleHelper.removeArticle(articleId, authorsToken);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Favorite article, check if field 'favorited' is set to true")
  @Test
  @DisplayName("Favorite article, check if favorited")
  void favoriteArticleCheckFavorited() {
    // GIVEN
    RequestSpecification requestSpecification = prepareRequestSpecification(articleId, followerToken);

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
}
