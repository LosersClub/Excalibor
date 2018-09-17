package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.EqualsArgument;

public class NullArgument extends EqualsArgument {
  
  @Override
  public Argument parse(String expression) {
    if (expression.equals("null")) {
      return this;
    }
    return null;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj == null) {
      return this;
    }
    return null;
  }
  
  @Override
  public Object getValue() {
    return null;
  }
  
  @Override
  public Argument convert(Argument vArg) {
    if (vArg.getValue() == null) {
      return this;
    }
    return null;
  }
  
  @Override
  public String toString() {
    return "null";
  }
  
  @Override
  public boolean equals(Object that) {
    return this == that || that == null || ((that instanceof Argument) &&
        ((Argument)that).getValue() == null);
  }

  @Override
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(rhs.getValue() == null);
  }
}
