package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;
import com.github.losersclub.excalibor.operator.UnaryOperator;

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
      throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" unary "
          + "operator. right-hand side is of type \"%s\" but must be of type \"%s\"",
          this.getSymbol(), rhs.getClass().getName(), LogicalArgument.class.getName()));
    }
    return ((LogicalArgument)rhs).not();
  }
}
