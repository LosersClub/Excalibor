package losers.club.excalibor.argument;

import losers.club.excalibor.argument.primitives.BooleanArgument;

public interface LogicalArgument extends Argument {
  BooleanArgument equals(Argument rhs);

  BooleanArgument and(Argument rhs);

  BooleanArgument or(Argument rhs);

  BooleanArgument xor(Argument rhs);

  BooleanArgument not();

  default BooleanArgument notEquals(Argument rhs) {
    return equals(rhs).not();
  }
}
