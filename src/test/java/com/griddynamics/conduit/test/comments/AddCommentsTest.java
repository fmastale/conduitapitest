package com.griddynamics.conduit.test.comments;

import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

import com.griddynamics.conduit.helpers.CommentHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.RequestSpecificationDetails;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Comment;
import com.griddynamics.conduit.jsonsdtos.CommentDto;
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
@Feature("Add Comments to an Article")
public class AddCommentsTest extends BaseTest {
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
  }

  @AfterEach
  void cleanup() {
    commentHelper.removeArticle(articleId, authorsToken);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Add comment to an article, check if response comment body is same as in request")
  @Test
  @DisplayName("Add comment to an article, check body")
  void addCommentToAnArticleCheckBody() {
    // GIVEN
    CommentDto requestComment = new CommentDto(new Comment("This is sample comment"));

    RequestSpecification requestSpecification =
        prepareRequestSpecification(requestComment, articleId, commenterToken);

    // WHEN
    CommentDto responseComment = getCommentFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual comment body is different than expected",
        responseComment.comment.body,
        Matchers.equalTo(requestComment.comment.body));
  }

  private CommentDto getCommentFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.post(Endpoint.ARTICLES_SLUG_COMMENTS.get()).as(CommentDto.class);
  }

  private RequestSpecification prepareRequestSpecification(
      CommentDto comment, String slug, String token) {

    return RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON.get())
        .header(RequestSpecificationDetails.AUTHORIZATION.get(), token)
        .pathParam(SLUG.get(), slug)
        .body(comment);
  }
}
