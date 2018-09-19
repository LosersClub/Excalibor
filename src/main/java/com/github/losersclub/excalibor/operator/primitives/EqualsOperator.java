package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;
import com.github.losersclub.excalibor.argument.NumberArgument;
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
    if (rhs instanceof LogicalArgument || rhs instanceof NumberArgument) {
      return rhs.equals(lhs);
    }
    return lhs.equals(rhs);
  }
}
