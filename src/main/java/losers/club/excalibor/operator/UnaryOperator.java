package losers.club.excalibor.operator;

import losers.club.excalibor.argument.Argument;

public abstract class UnaryOperator extends Operator {
  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs != null) {
      throw new IllegalArgumentException(String.format("Only one argument allowed for operation %s",
          this.getSymbol()));
    }
    return evaluate(rhs);
  }

  public abstract Argument evaluate(Argument rhs);
}
