package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import com.griddynamics.conduit.jsonsdtos.ArticlesListDto;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
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

import static com.griddynamics.conduit.helpers.Endpoint.*;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.*;

@Epic("Smoke tests")
@Feature("Feed Articles")
public class FeedArticlesTest {
  private static String followersToken;
  private static UserResponseDto author;
  private static ArticleDto article;

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();
  }

  @BeforeEach
  void createAuthorArticleFollower(){
    author = registerUser(new TestDataProvider().getValidRegistrationUser());
    article = createArticle(author);

    followersToken = getFollowersToken();
    startFollowingAuthor(followersToken, author);
  }

  @AfterEach
  void stopFollowing() {
    stopFollowingUser(followersToken, author);
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
        Matchers.equalTo(getRequestAuthorName(article)));
  }


  private String getRequestAuthorName(ArticleDto articles) {
    return articles.article.author.username;
  }

  private String getResponseAuthorName(ArticlesListDto articles) {
    return articles.articles.get(0).author.username;
  }

  private ArticlesListDto getArticlesFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(Endpoint.ARTICLES_FEED.get()).as(ArticlesListDto.class);
  }

  private RequestSpecification prepareRequestSpecification(String followersToken) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.getDetails())
        .header(AUTHORIZATION.getDetails(), followersToken);
  }

  private void startFollowingAuthor(String token, UserResponseDto author) {
    RequestSpecification requestSpecification = prepareRequestSpecification(token, author);

    int statusCode = requestSpecification
            .contentType("application/json")
            .post(PROFILES_USERNAME_FOLLOW.get())
            .statusCode();

    // todo: extract this method
    if (not200(statusCode)) {
      throw new IllegalStateException("Author is not followed");
    }
  }

  private ArticleDto createArticle(UserResponseDto author) {
    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType(APPLICATION_JSON.getDetails())
            .header(AUTHORIZATION.getDetails(), "Token " + author.user.token)
            .body(new Article("This is article title", "This is description", "This is body"));

    Response response = requestSpecification.post(ARTICLES.get());
    return response.as(ArticleDto.class);
  }

  private UserResponseDto registerUser(RegistrationRequestUser author) {
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(author);
    RequestSpecification requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetails()).body(requestBody);

    return requestSpecification.post(USERS.get()).as(UserResponseDto.class);
  }

  private String getFollowersToken() {
    return new TokenProvider().getTokenForUser(new TestDataProvider().getTestUser());
  }

  private static RequestSpecification prepareRequestSpecification(
      String followersToken, UserResponseDto author) {
    return RestAssured.given()
        .header(AUTHORIZATION.getDetails(), followersToken)
        .pathParam(USERNAME.getDetails(), author.user.username);
  }

  private boolean not200(int statusCode) {
    return statusCode != 200;
  }

  private void stopFollowingUser(String  followersToken, UserResponseDto author) {
    RequestSpecification requestSpecification = prepareRequestSpecification(followersToken, author);

    int statusCode = requestSpecification
        .contentType("application/json")
        .delete(PROFILES_USERNAME_FOLLOW.get())
        .statusCode();

    if (not200(statusCode)) {
      throw new IllegalStateException("Author is still followed");
    }
  }
}
