package losers.club.excalibor.argument;

import losers.club.excalibor.argument.primitives.BooleanArgument;

public abstract class EqualsArgument extends Argument {
  public abstract BooleanArgument equals(Argument rhs);
  
  public BooleanArgument notEquals(Argument rhs) {
    return equals(rhs).not();
  }
}
