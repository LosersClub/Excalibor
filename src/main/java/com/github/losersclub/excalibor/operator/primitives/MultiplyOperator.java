package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;
import com.github.losersclub.excalibor.operator.MathOperator;

public class MultiplyOperator extends MathOperator {

  @Override
  public String getSymbol() {
    return "*";
  }

  @Override
  public Argument evaluateMath(NumberArgument lhs, Argument rhs) {
    return lhs.multiply(rhs);
  }

}