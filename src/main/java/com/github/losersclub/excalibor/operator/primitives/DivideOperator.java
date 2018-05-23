package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;
import com.github.losersclub.excalibor.operator.MathOperator;

public class DivideOperator extends MathOperator {

  @Override
  public String getSymbol() {
    return "/";
  }

  @Override
  public Argument evaluateMath(NumberArgument lhs, Argument rhs) {
    if (rhs instanceof NumberArgument && ((NumberArgument) rhs).getMathTypeValue() == 0) {
      throw new IllegalArgumentException("Invalid value: Divide by zero");
    }
    return lhs.divide(rhs);
  }

}
