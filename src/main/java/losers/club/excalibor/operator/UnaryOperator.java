package losers.club.excalibor.operator;

import losers.club.excalibor.argument.Argument;

public abstract class UnaryOperator extends Operator {
  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    // Ignores lhs argument (should be null?)
    return evaluate(rhs);
  }
  
  public abstract Argument evaluate(Argument rhs);
}
