package com.github.losersclub.excalibor.operator.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;
import com.github.losersclub.excalibor.operator.Operator;

public class AddOperator extends Operator {

  @Override
  public String getSymbol() {
    return "+";
  }

  @Override
  public int priority() {
    return 11;
  }

  @Override
  public Argument evaluate(Argument lhs, Argument rhs) {
    if (lhs instanceof StringArgument) {
      return (((StringArgument)lhs).concat(new StringArgument(rhs.getValue().toString())));
    }
    if (rhs instanceof StringArgument) {
      return (new StringArgument(lhs.getValue().toString())).concat(rhs);
    }
    if (lhs instanceof NumberArgument) {
      return ((NumberArgument) lhs).add(rhs);
    }

    throw new IllegalArgumentException(String.format("Incompatible types for %s operation",
        this.getSymbol()));

  }

}
