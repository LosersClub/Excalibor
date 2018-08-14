package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;

public abstract class LogicOperator extends Operator {

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (!(lhs instanceof LogicalArgument)) {
      throw new InvalidExpressionException(String.format("Incompatible types for \"%s\" operator. "
          + "left-hand side is of type \"%s\" but must be of type \"%s\"",
          this.getSymbol(), lhs.getClass().getName(), LogicalArgument.class.getName()));
    }
    return evaluateLogic((LogicalArgument)lhs, rhs);
  }

  public abstract Argument evaluateLogic(LogicalArgument lhs, Argument rhs);
}
