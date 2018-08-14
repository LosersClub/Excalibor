package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.ComparableArgument;

public abstract class CompareOperator extends Operator {
  
  @Override
  public int priority() {
    return 9;
  }
  
  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (!(lhs instanceof ComparableArgument)) {
      throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" operator. "
          + "left-hand side is of type \"%s\" but must be of type \"%s\"",
          this.getSymbol(), lhs.getClass().getName(), ComparableArgument.class.getName()));
    }
    return evaluateCompare((ComparableArgument)lhs, rhs);
  }

  public abstract Argument evaluateCompare(ComparableArgument lhs, Argument rhs);
}
