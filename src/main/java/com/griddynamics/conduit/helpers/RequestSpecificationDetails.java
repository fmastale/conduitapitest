package com.griddynamics.conduit.helpers;

public enum RequestSpecificationDetails {

  APPLICATION_JSON("application/json"),
  AUTHORIZATION("Authorization"),
  USERNAME("username"),
  SLUG("slug");

  private final String detail;

  RequestSpecificationDetails(String detail) {
    this.detail = detail;
  }

  public String getDetail() {
    return detail;
  }
}