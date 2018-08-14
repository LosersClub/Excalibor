package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.InvalidExpressionException;
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
      throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" operator. "
          + "left-hand side is of type \"%s\" but must be of type \"%s\"",
          this.getSymbol(), lhs.getClass().getName(), NumberArgument.class.getName()));
    }
    return evaluateMath((NumberArgument)lhs, rhs);
  }

  public abstract Argument evaluateMath(NumberArgument lhs, Argument rhs);

}
