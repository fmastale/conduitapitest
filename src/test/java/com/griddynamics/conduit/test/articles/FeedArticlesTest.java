package com.griddynamics.conduit.test.articles;

import static com.griddynamics.conduit.helpers.Endpoint.PROFILES_USERNAME_FOLLOW;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.USERNAME;

import com.griddynamics.conduit.helpers.ArticleHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ArticlesListDto;
import com.griddynamics.conduit.test.BaseTest;
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
@Feature("Feed Articles")
public class FeedArticlesTest extends BaseTest {

  private static String followersToken;
  private static UserRequest follower = testDataProvider.getTestUserTwo();

  private ArticleHelper articleHelper = new ArticleHelper(token);

  @BeforeAll
  static void prepareEnvironment() {
    followersToken = new TokenProvider().getTokenForUser(follower);
  }

  @BeforeEach
  void setup() {
    Response response =
        articleHelper.createArticle(new Article("Title", "Description", "Body"));
    articleHelper.checkIfSucceeded(response);

    startFollowingAuthor(followersToken, user);
  }

  @AfterEach
  void cleanup() {
    stopFollowingUser(followersToken, user);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Get articles from feed, check if author name is same in actual and expected")
  @Test
  @DisplayName("Get feed articles, check author name")
  void getFeedCheckName() {
    // GIVEN
    RequestSpecification requestSpecification = prepareRequestSpecification(followersToken);

    // WHEN
    ArticlesListDto feedArticles = getArticlesFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual slug is different than expected",
        getResponseAuthorName(feedArticles),
        Matchers.equalTo(user.username));
  }

  private String getResponseAuthorName(ArticlesListDto articles) {
    return articles.articles.get(0).author.username;
  }

  private ArticlesListDto getArticlesFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(Endpoint.ARTICLES_FEED.get()).as(ArticlesListDto.class);
  }

  private void startFollowingAuthor(String token, UserRequest author) {
    RequestSpecification requestSpecification = prepareRequestSpecification(token, author);
    checkIfFollowed(requestSpecification);
  }

  private void checkIfFollowed(RequestSpecification requestSpecification) {
    Response response =
        requestSpecification.contentType("application/json").post(PROFILES_USERNAME_FOLLOW.get());

    articleHelper.checkIfSucceeded(response);
  }

  private static RequestSpecification prepareRequestSpecification(
      String followersToken, UserRequest author) {

    return RestAssured.given()
        .header(AUTHORIZATION.get(), followersToken)
        .pathParam(USERNAME.get(), author.username);
  }

  private RequestSpecification prepareRequestSpecification(String followersToken) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.get())
        .header(AUTHORIZATION.get(), followersToken);
  }

  private void stopFollowingUser(String followersToken, UserRequest author) {
    RequestSpecification requestSpecification = prepareRequestSpecification(followersToken, author);

    Response response =
        requestSpecification.contentType("application/json").delete(PROFILES_USERNAME_FOLLOW.get());

    articleHelper.checkIfSucceeded(response);
  }
}
