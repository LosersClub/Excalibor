package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;

public abstract class MathOperator extends Operator {

  @Override
  public int priority() {
    return 12;
  }
  
  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (!(lhs instanceof NumberArgument)) {
      throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
          this.getSymbol()));
    }
    return evaluateMath((NumberArgument)lhs, rhs);
  }

  public abstract Argument evaluateMath(NumberArgument lhs, Argument rhs);

}
