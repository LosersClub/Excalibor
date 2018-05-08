package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.LogicalArgument;
import losers.club.excalibor.operator.LogicOperator;

public class XOrOperator extends LogicOperator {

  @Override
  public String getSymbol() {
    return "^";
  }
  
  @Override
  public int priority() {
    return 6;
  }

  @Override
  public Argument evaluateLogic(LogicalArgument lhs, Argument rhs) {
    return lhs.xor(rhs);
  }

}
