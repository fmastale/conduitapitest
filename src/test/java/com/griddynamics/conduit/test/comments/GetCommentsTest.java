package com.griddynamics.conduit.test.comments;

import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

import com.griddynamics.conduit.helpers.CommentHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.RequestSpecificationDetails;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Comment;
import com.griddynamics.conduit.jsonsdtos.CommentDto;
import com.griddynamics.conduit.jsonsdtos.CommentsDto;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Get Comments from an Article")
public class GetCommentsTest extends BaseTest {

  private static String authorsToken;
  private static String commenterToken;

  private String articleId;
  private CommentHelper commentHelper = new CommentHelper();

  @BeforeAll
  static void prepareEnvironment() {
    authorsToken = token;
    commenterToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserTwo());
  }

  @BeforeEach
  void setup() {
    articleId = commentHelper.getSlugFromCreatedArticle(authorsToken);
    createCommentsForArticle(commenterToken, articleId);
  }

  @AfterEach
  void cleanup() {
    commentHelper.removeArticle(articleId, authorsToken);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get comments from article, check if there are two of them")
  @Test
  @DisplayName("Get comments from article, check amount")
  void getCommentFromArticleCheckAmount() {
    // GIVEN
    RequestSpecification requestSpecification = getCommentRequest(authorsToken, articleId);

    // WHEN
    CommentsDto commentsDto = getCommentsFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual amount of comments is different than expected",
        commentsDto.comments.length,
        Matchers.equalTo(2));
  }

  private CommentsDto getCommentsFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(Endpoint.ARTICLES_SLUG_COMMENTS.get()).as(CommentsDto.class);
  }

  private RequestSpecification getCommentRequest(String token, String articleId) {
    return RestAssured.given()
        .header(RequestSpecificationDetails.AUTHORIZATION.get(), token)
        .pathParam(SLUG.get(), articleId);
  }

  private RequestSpecification commentRequestSpecification(
      String token, String slug, CommentDto requestComment) {

    return RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON.get())
        .header(RequestSpecificationDetails.AUTHORIZATION.get(), token)
        .pathParam(SLUG.get(), slug)
        .body(requestComment);
  }

  private void createCommentsForArticle(String token, String slug) {
    CommentDto requestComment = new CommentDto(new Comment("This is sample comment"));

    RequestSpecification requestSpecification =
        commentRequestSpecification(token, slug, requestComment);

    for (int i = 0; i < 2; i++) {
      requestSpecification.post(Endpoint.ARTICLES_SLUG_COMMENTS.get()).as(CommentDto.class);
    }
  }
}
