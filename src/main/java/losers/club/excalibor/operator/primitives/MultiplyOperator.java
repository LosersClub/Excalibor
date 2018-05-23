package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.NumberArgument;
import losers.club.excalibor.operator.MathOperator;

public class MultiplyOperator extends MathOperator {

  @Override
  public String getSymbol() {
    return "*";
  }

  @Override
  public Argument evaluateMath(NumberArgument lhs, Argument rhs) {
    return lhs.multiply(rhs);
  }

}