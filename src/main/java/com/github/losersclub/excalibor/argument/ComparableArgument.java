package com.github.losersclub.excalibor.argument;

import com.github.losersclub.excalibor.argument.primitives.BooleanArgument;

public abstract class ComparableArgument extends Argument {
  public abstract BooleanArgument lessThan(Argument rhs);

  public BooleanArgument lessThanEqualTo(Argument rhs) {
    return greaterThan(rhs).not();
  }

  public abstract BooleanArgument greaterThan(Argument rhs);

  public BooleanArgument greaterThanEqualTo(Argument rhs) {
    return lessThan(rhs).not();
  }
}
