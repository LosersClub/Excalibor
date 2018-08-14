package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.ComparableArgument;
import com.github.losersclub.excalibor.operator.CompareOperator;

public class GreaterThanEqOperator extends CompareOperator {

  @Override
  public String getSymbol() {
    return ">=";
  }

  @Override
  public Argument evaluateCompare(ComparableArgument lhs, Argument rhs) {
    return lhs.greaterThanEqualTo(rhs);
  }
}
