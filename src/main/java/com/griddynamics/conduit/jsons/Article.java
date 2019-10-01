package com.griddynamics.conduit.jsons;

import java.util.List;

public class Article {
  public String slug;
  public String title;
  public String description;
  public String body;
  public List<String> tagList;
  public String createdAt;
  public String updatedAt;
  public boolean favorited;
  public int favoritesCount;
  public Author author;
}
