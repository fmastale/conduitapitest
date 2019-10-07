package com.griddynamics.conduit.jsons;

public class Comment {
  public int id;
  public String createdAt;
  public String updatedAt;
  public String body;
  public Author author;

  public Comment() {
  }

  public Comment(String body) {
    this.body = body;
  }
}
