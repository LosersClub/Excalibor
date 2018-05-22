package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.ComparableArgument;
import losers.club.excalibor.operator.CompareOperator;

public class LessThanEqOperator extends CompareOperator {

  @Override
  public String getSymbol() {
    return "<=";
  }

  @Override
  public Argument evaluateCompare(ComparableArgument lhs, Argument rhs) {
    return lhs.lessThanEqualTo(rhs);
  }

}
