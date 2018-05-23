package com.github.losersclub.excalibor;

public class AmbiguousOperatorException extends RuntimeException {
  private static final long serialVersionUID = 8117269221473946230L;

  public AmbiguousOperatorException(String message) {
    super(message);
  }
}