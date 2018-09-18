package com.github.losersclub.excalibor.argument.primitives;

import java.util.Objects;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.EqualsArgument;

public class GenericArgument extends EqualsArgument {
  private final Object value;
  
  public GenericArgument(Object value) {
    this.value = value;
  }
  
  @Override
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(Objects.equals(this.getValue(), rhs.getValue()));
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
