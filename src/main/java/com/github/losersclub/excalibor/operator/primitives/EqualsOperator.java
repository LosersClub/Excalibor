package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.EqualsArgument;
import com.github.losersclub.excalibor.operator.Operator;

public class EqualsOperator extends Operator {

  @Override
  public String getSymbol() {
    return "==";
  }
  
  @Override
  public int priority() {
    return 8;
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs instanceof EqualsArgument) {
      return ((EqualsArgument)lhs).equals(rhs);
    }
    throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" operator. "
        + "left-hand side is of type \"%s\" and right-hand side is of type \"%s\", but only "
        + "\"%s\" are supported.", this.getSymbol(), lhs.getClass().getName(),
        rhs.getClass().getName(), EqualsArgument.class.getName()));
  }
}
