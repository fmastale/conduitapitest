package com.griddynamics.conduit.jsons;

public class Article {
  public String slug;
  public String title;
  public String description;
  public String body;
  public String[] tagList;
  public String createdAt;
  public String updatedAt;
  public boolean favorited;
  public int favoritesCount;
  public Author author;

  public Article() {}

  public Article(String title, String description, String body) {
    this.title = title;
    this.description = description;
    this.body = body;
  }

  public Article(String title, String description, String body, String[] tagList) {
    this.title = title;
    this.description = description;
    this.body = body;
    this.tagList = tagList;
  }
}
