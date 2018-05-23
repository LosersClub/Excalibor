package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;
import com.github.losersclub.excalibor.operator.LogicOperator;

public class AndOperator extends LogicOperator{
  
  @Override
  public String getSymbol() {
    return "&&";
  }
  
  @Override
  public int priority() {
    return 4;
  }

  @Override
  public Argument evaluateLogic(LogicalArgument lhs, Argument rhs) {
    return lhs.and(rhs);
  }

}
