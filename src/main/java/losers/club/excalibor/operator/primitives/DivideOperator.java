package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.NumberArgument;
import losers.club.excalibor.operator.MathOperator;

public class DivideOperator extends MathOperator {

  @Override
  public String getSymbol() {
    return "/";
  }

  @Override
  public Argument evaluateMath(NumberArgument lhs, Argument rhs) {
    if (rhs instanceof NumberArgument && ((NumberArgument) rhs).getMathTypeValue() == 0) {
      throw new IllegalArgumentException("Invalid value: Divide by zero");
    }
    return lhs.divide(rhs);
  }

}
