package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;
import com.github.losersclub.excalibor.operator.UnaryOperator;

public class NegateOperator extends UnaryOperator {

  @Override
  public String getSymbol() {
    return "-";
  }
  
  @Override
  public int priority() {
    return 11;
  }

  @Override
  public Argument evaluate(Argument rhs) {
    if (!(rhs instanceof NumberArgument)) {
      throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" unary "
          + "operator. right-hand side is of type \"%s\" but must be of type \"%s\"",
          this.getSymbol(), rhs.getClass().getName(), NumberArgument.class.getName()));
    }
    return ((NumberArgument)rhs).negate();
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs == null) {
      return this.evaluate(rhs);
    }
    if (!(lhs instanceof NumberArgument)) {
      throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" operator. "
          + "left-hand side is of type \"%s\" but must be of type \"%s\"",
          this.getSymbol(), lhs.getClass().getName(), NumberArgument.class.getName()));
    }
    return ((NumberArgument)lhs).subtract(rhs);
  }
}
