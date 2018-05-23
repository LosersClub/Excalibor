package com.github.losersclub.excalibor.operator.primitives;

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
      throw new IllegalArgumentException(String.format("Incompatible type for %s operation",
          this.getSymbol()));
    }
    return ((LogicalArgument)rhs).not();
  }

}
