package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public interface NumberArgument extends ComparableArgument {
  Argument add(Argument rhs);
  Argument subtract(Argument rhs);
  Argument multiply(Argument rhs);
  Argument divide(Argument rhs);
  Argument modulo(Argument rhs);
  double getMathTypeValue();
}
