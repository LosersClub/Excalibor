package com.github.losersclub.excalibor.argument;

import com.github.losersclub.excalibor.argument.primitives.NullArgument;

public abstract class Argument {

  public abstract Argument parse(String expression);
  public abstract Argument build(Object obj);
  public abstract Object getValue();
  
  public Argument convert(Argument vArg) {
    if (vArg.getValue() == null) {
      return new NullArgument();
    }
    if (this.getValue() == null || vArg.getValue() == null ||
        vArg.getValue().getClass() != this.getValue().getClass()) {
      return null;
    }
    return build(vArg.getValue());
  }
  
  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (!(that instanceof Argument)) {
      return this.getValue().equals(that);
    }
    return this.getValue().equals(((Argument)that).getValue());
  }
  
  @Override
  public String toString() {
    return this.getValue().toString();
  }
}
