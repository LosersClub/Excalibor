package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;

public abstract class UnaryOperator extends Operator {
  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs != null) {
      throw new InvalidExpressionException("Operator \"" + this.getSymbol() + "\" cannot be used "
          + "in a non-unary context.");
    }
    return evaluate(rhs);
  }

  public abstract Argument evaluate(Argument rhs);
}
