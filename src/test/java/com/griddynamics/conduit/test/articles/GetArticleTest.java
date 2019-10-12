package com.griddynamics.conduit.test.articles;

import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

import com.griddynamics.conduit.helpers.ArticleHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
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
@Feature("Get Article")
public class GetArticleTest extends BaseTest {

  private String slug;
  private ArticleHelper articleHelper = new ArticleHelper(token);

  @BeforeEach
  void setup() {
    slug = articleHelper.getSlugFromCreatedArticle(token);
  }

  @AfterEach
  void cleanup() {
    articleHelper.removeArticle(slug, token);
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
    return RestAssured.given().pathParam(SLUG.get(), slug);
  }
}
