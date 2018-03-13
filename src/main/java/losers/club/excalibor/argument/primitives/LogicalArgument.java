package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public interface LogicalArgument extends Argument {
  BooleanArgument equals(Argument rhs);

  BooleanArgument and(Argument rhs);

  BooleanArgument or(Argument rhs);

  BooleanArgument xor(Argument rhs);

  default BooleanArgument notEquals(Argument rhs) {
    return equals(rhs).not();
  }
}
