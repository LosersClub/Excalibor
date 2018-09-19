package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.argument.Argument;

public class GenericArgument extends Argument {
  private final Object value;
  
  public GenericArgument(Object value) {
    this.value = value;
  }

  @Override
  public Argument parse(String expression) {
    return null;
  }

  @Override
  public Argument build(Object obj) {
    return null;
  }

  @Override
  public Object getValue() {
    return this.value;
  }
}
