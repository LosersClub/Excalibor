package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.ComparableArgument;
import losers.club.excalibor.argument.primitives.LogicalArgument;
import losers.club.excalibor.operator.Operator;

public class NotEqualsOperator extends Operator {

  @Override
  public String getSymbol() {
    return "!=";
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs instanceof LogicalArgument) {
      return ((LogicalArgument)lhs).notEquals(rhs);
    }
    if (lhs instanceof ComparableArgument) {
      return ((ComparableArgument)lhs).notEquals(rhs);
    }
    throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
        this.getSymbol()));
  }

}
