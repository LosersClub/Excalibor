package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.ComparableArgument;
import losers.club.excalibor.argument.LogicalArgument;
import losers.club.excalibor.operator.Operator;

public class EqualsOperator extends Operator {

  @Override
  public String getSymbol() {
    return "==";
  }
  
  @Override
  public int priority() {
    return 8;
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs instanceof LogicalArgument) {
      return ((LogicalArgument)lhs).equals(rhs);
    }
    if (lhs instanceof ComparableArgument) {
      return ((ComparableArgument)lhs).equals(rhs);
    }
    throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
        this.getSymbol()));
  }

}
