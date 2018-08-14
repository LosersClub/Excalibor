package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.LogicalArgument;

public class BooleanArgument extends LogicalArgument {
  private final boolean value;

  public BooleanArgument() {
    this(false);
  }

  public BooleanArgument(boolean value) {
    this.value = value;
  }
  
  @Override
  public Argument build(Object obj) {
    return new BooleanArgument((boolean)obj);
  }

  @Override
  public BooleanArgument not() {
    return new BooleanArgument(!this.value);
  }

  @Override
  public Argument parse(String expression) {
    expression = expression.toLowerCase();
    if (expression.equals("true") ^ expression.equals("false")) {
      return new BooleanArgument(Boolean.valueOf(expression));
    }
    return null;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(this.value == getRhsValue("==", rhs));
  }

  @Override
  public BooleanArgument and(Argument rhs) {
    return new BooleanArgument(this.value && getRhsValue("&&", rhs));
  }

  @Override
  public BooleanArgument or(Argument rhs) {
    return new BooleanArgument(this.value || getRhsValue("||", rhs));
  }

  @Override
  public BooleanArgument xor(Argument rhs) {
    return new BooleanArgument(this.value ^ getRhsValue("^ [xor]", rhs));
  }

  private boolean getRhsValue(String op, Argument rhs) {
    if (rhs.getValue() instanceof Boolean) {
      return (boolean)rhs.getValue();
    }
    throw new InvalidExpressionException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        Boolean.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

}
