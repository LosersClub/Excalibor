package losers.club.excalibor.argument;

import losers.club.excalibor.argument.primitives.BooleanArgument;

public interface ComparableArgument extends Argument{
  BooleanArgument lessThan(Argument rhs);

  default BooleanArgument lessThanEqualTo(Argument rhs) {
    return greaterThan(rhs).not();
  }

  BooleanArgument greaterThan(Argument rhs);

  default BooleanArgument greaterThanEqualTo(Argument rhs) {
    return lessThan(rhs).not();
  }

  BooleanArgument equals(Argument rhs);

  default BooleanArgument notEquals(Argument rhs) {
    return equals(rhs).not();
  }
}
