package com.griddynamics.conduit.helpers;

public enum StatusCode {

  CODE_200(200),
  CODE_422(422),
  CODE_404(404);

  private final int value;

  StatusCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
