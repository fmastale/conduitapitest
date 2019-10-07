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
import com.griddynamics.conduit.jsons.Comment;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import com.griddynamics.conduit.jsonsdtos.CommentDto;
import com.griddynamics.conduit.jsonsdtos.CommentsDto;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
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
@Feature("Get Comments from an Article")
public class GetCommentsTest {

  private static String authorsToken;
  private static String commenterToken;
  private String slug;

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    authorsToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserOne());
    commenterToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserTwo());
  }

  @BeforeEach
  void setup() {
    Article article = new Article("Title", "Description", "Body");
    slug = getSlugFromCreatedArticle(article, authorsToken);

    createCommentsForArticle(commenterToken, slug);
  }

  private void createCommentsForArticle(String token, String slug) {
    CommentDto requestComment = new CommentDto(new Comment("This is sample comment"));

    RequestSpecification requestSpecification =
        commentRequestSpecification(token, slug, requestComment);

    // Create comment 1
    requestSpecification.post(Endpoint.ARTICLES_SLUG_COMMENTS.get()).as(CommentDto.class);

    // Create comment 2
    requestSpecification.post(Endpoint.ARTICLES_SLUG_COMMENTS.get()).as(CommentDto.class);
  }

  @AfterEach
  void cleanup() {
    removeArticle(slug, authorsToken);
  }

  @Test
  @DisplayName("Add comment to an article, check something")
  void addCommentToAnArticle() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
            .header(RequestSpecificationDetails.AUTHORIZATION.get(), authorsToken)
            .pathParam(SLUG.get(), slug);

    // WHEN
    CommentsDto commentsDto =
        requestSpecification.get(Endpoint.ARTICLES_SLUG_COMMENTS.get()).as(CommentsDto.class);

    // THEN
    MatcherAssert.assertThat("", commentsDto.comments.length, Matchers.equalTo(2));
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

  private RequestSpecification commentRequestSpecification(
      String token, String slug, CommentDto requestComment) {

    return RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON.get())
        .header(RequestSpecificationDetails.AUTHORIZATION.get(), token)
        .pathParam(SLUG.get(), slug)
        .body(requestComment);
  }
}
