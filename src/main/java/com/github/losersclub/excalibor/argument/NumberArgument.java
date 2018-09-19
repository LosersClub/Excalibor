package com.github.losersclub.excalibor.argument;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.primitives.BooleanArgument;

public abstract class NumberArgument extends ComparableArgument {
  
  public Argument add(Argument rhs) {
    return cast(rhs, this.add(getRhsValue("add", rhs)));
  }
  public Argument subtract(Argument rhs) {
    return cast(rhs, this.subtract(getRhsValue("subtract", rhs)));
  }
  public Argument multiply(Argument rhs) {
    return cast(rhs, this.multiply(getRhsValue("multiply", rhs)));
  }
  public Argument divide(Argument rhs) {
    return cast(rhs, this.divide(getRhsValue("divide", rhs)));
  }
  public Argument modulo(Argument rhs) {
    return cast(rhs, this.modulo(getRhsValue("modulo", rhs)));
  }
  public abstract Argument negate();
  
  public abstract int priority();
  public abstract double getMathTypeValue();
  protected abstract double add(double rhs);
  protected abstract double subtract(double rhs);
  protected abstract double multiply(double rhs);
  protected abstract double divide(double rhs);
  protected abstract double modulo(double rhs);
  
  @Override
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(this.getMathTypeValue() == getRhsValue("==", rhs));
  }
  
  protected double getRhsValue(String op, Argument rhs) {
    if (rhs instanceof NumberArgument) {
      return ((NumberArgument)(rhs)).getMathTypeValue();
    }
    throw new InvalidExpressionException(String.format(
        "Incompatible types for \"%s\" operator: right-hand side is of type \"%s\", but only "
        + "\"%s\" is supported", op, rhs.getClass().getName(),
        NumberArgument.class.getName()));
  }
  
  private Argument cast(Argument rhs, double val) {
    return this.priority() > ((NumberArgument)rhs).priority() ? this.build(val) : rhs.build(val);
  }
}
