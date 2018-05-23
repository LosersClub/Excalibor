package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.ComparableArgument;
import com.github.losersclub.excalibor.argument.LogicalArgument;
import com.github.losersclub.excalibor.operator.Operator;

public class NotEqualsOperator extends Operator {

  @Override
  public String getSymbol() {
    return "!=";
  }
  
  @Override
  public int priority() {
    return 8;
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs instanceof LogicalArgument) {
      return ((LogicalArgument)lhs).notEquals(rhs);
    }
    if (lhs instanceof ComparableArgument) {
      return ((ComparableArgument)lhs).notEquals(rhs);
    }
    throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
        this.getSymbol()));
  }

}
