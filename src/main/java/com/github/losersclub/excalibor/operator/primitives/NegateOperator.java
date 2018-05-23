package com.github.losersclub.excalibor.operator.primitives;

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
      throw new IllegalArgumentException(String.format("Incompatible type for %s operation",
          this.getSymbol()));
    }
    return ((NumberArgument)rhs).negate();
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs == null) {
      return this.evaluate(rhs);
    }
    if (!(lhs instanceof NumberArgument)) {
      throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
          this.getSymbol()));
    }
    return ((NumberArgument)lhs).subtract(rhs);
  }

}