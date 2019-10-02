package com.griddynamics.conduit.jsonsdtos;

import com.griddynamics.conduit.jsons.Article;
import java.util.Objects;

public class ArticleDto {
  public Article article;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleDto that = (ArticleDto) o;
    return Objects.equals(article, that.article);
  }

  @Override
  public int hashCode() {
    return Objects.hash(article);
  }
}
