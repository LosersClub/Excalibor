package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;

public class DoubleArgument extends NumberArgument {
  private final double value;

  public DoubleArgument() {
    this(0.0);
  }

  public DoubleArgument(double value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 60;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new DoubleArgument(((Number)obj).doubleValue());
    }
    throw new InvalidExpressionException(obj.getClass().getSimpleName() + " must of type Number");
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    int decimalCount = 0;
    for (int i = 0; i < expression.length(); i++) {
      if (expression.charAt(i) == '.') {
        decimalCount++;;
      } else if(!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    if (decimalCount == 1) {
      return new DoubleArgument(Double.valueOf(expression));
    }
    return null;
  }

  @Override
  protected double add(double rhs) {
    return this.value + rhs;
  }
  
  @Override
  protected double subtract(double rhs) {
    return this.value - rhs;
  }
  
  @Override
  protected double multiply(double rhs) {
    return this.value * rhs;
  }
  
  @Override
  protected double divide(double rhs) {
    return this.value / rhs;
  }
  
  @Override
  protected double modulo(double rhs) {
    return this.value % rhs;
  }

  @Override
  public BooleanArgument lessThan(Argument rhs) {
    return new BooleanArgument(this.value < getRhsValue("<", rhs));
  }

  @Override
  public BooleanArgument greaterThan(Argument rhs) {
    return new BooleanArgument(this.value > getRhsValue(">", rhs));
  }

  @Override
  public double getMathTypeValue() {
    return this.value;
  }

  @Override
  public Argument negate() {
    return new DoubleArgument(0 - this.value);
  }

}