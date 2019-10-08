package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;

import com.griddynamics.conduit.helpers.CommentHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.RequestSpecificationDetails;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
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
@Feature("Delete comment")
public class DeleteCommentTest {

  private static String authorsToken;
  private static String commenterToken;

  private int commentId;
  private String articleId;
  private CommentHelper commentHelper = new CommentHelper();

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    authorsToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserOne());
    commenterToken = new TokenProvider().getTokenForUser(new TestDataProvider().getTestUserTwo());
  }

  @BeforeEach
  void setup() {
    articleId = commentHelper.getSlugFromCreatedArticle(authorsToken);
    commentId = commentHelper.commentArticle(commenterToken, articleId);
  }

  @AfterEach
  void cleanup() {
    commentHelper.removeArticle(articleId, authorsToken);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Delete article, check if status code is 200")
  @Test
  @DisplayName("Delete article, check status code")
  void deleteArticleCheckStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification =
        removeCommentSpecification(commenterToken, articleId, commentId);

    // WHEN
    int statusCode = getStatusCode(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected", statusCode, Matchers.equalTo(200));
  }


  private RequestSpecification removeCommentSpecification(
      String commenterToken, String articleId, int commentId) {
    return RestAssured.given()
        .header(AUTHORIZATION.get(), commenterToken)
        .pathParam(RequestSpecificationDetails.SLUG.get(), articleId)
        .pathParam(RequestSpecificationDetails.ID.get(), commentId);
  }

  private int getStatusCode(RequestSpecification requestSpecification) {
    return requestSpecification.delete(Endpoint.ARTICLES_SLUG_COMMENTS_ID.get()).statusCode();
  }
}
