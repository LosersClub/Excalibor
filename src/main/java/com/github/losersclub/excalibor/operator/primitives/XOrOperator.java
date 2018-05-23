package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;
import com.github.losersclub.excalibor.operator.LogicOperator;

public class XOrOperator extends LogicOperator {

  @Override
  public String getSymbol() {
    return "^";
  }
  
  @Override
  public int priority() {
    return 6;
  }

  @Override
  public Argument evaluateLogic(LogicalArgument lhs, Argument rhs) {
    return lhs.xor(rhs);
  }

}
