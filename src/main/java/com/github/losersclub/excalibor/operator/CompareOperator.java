package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.ComparableArgument;

public abstract class CompareOperator extends Operator {
  
  @Override
  public int priority() {
    return 9;
  }
  
  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (!(lhs instanceof ComparableArgument)) {
      throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
          this.getSymbol()));
    }
    return evaluateCompare((ComparableArgument)lhs, rhs);
  }

  public abstract Argument evaluateCompare(ComparableArgument lhs, Argument rhs);
}
