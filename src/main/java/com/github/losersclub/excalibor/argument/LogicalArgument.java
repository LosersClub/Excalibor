package com.github.losersclub.excalibor.argument;

import com.github.losersclub.excalibor.argument.primitives.BooleanArgument;

public abstract class  LogicalArgument extends EqualsArgument {
  public abstract BooleanArgument and(Argument rhs);

  public abstract BooleanArgument or(Argument rhs);

  public abstract BooleanArgument xor(Argument rhs);

  public abstract BooleanArgument not();
}
