package com.github.losersclub.excalibor;

public class InvalidExpressionException extends RuntimeException {
  private static final long serialVersionUID = 1196848382366892909L;

  public InvalidExpressionException(String message) {
    super(message);
  }
}
