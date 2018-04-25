package losers.club.excalibor.operator.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.LogicalArgument;
import losers.club.excalibor.operator.UnaryOperator;

public class NotOperator extends UnaryOperator {

  @Override
  public String getSymbol() {
    return "!";
  }
  
  @Override
  public int priority() {
   return 14;
  }

  @Override
  public Argument evaluate(Argument rhs) {
    if (!(rhs instanceof LogicalArgument)) {
      throw new IllegalArgumentException(String.format("Incompatible type for %s operation",
          this.getSymbol()));
    }
    return ((LogicalArgument)rhs).not();
  }

}
