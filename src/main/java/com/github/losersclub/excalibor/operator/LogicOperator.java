package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;

public abstract class LogicOperator extends Operator {

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (!(lhs instanceof LogicalArgument)) {
      throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
          this.getSymbol()));
    }
    return evaluateLogic((LogicalArgument)lhs, rhs);
  }

  public abstract Argument evaluateLogic(LogicalArgument lhs, Argument rhs);
}
