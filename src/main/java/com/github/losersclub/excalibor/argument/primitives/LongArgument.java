package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;

public class LongArgument extends NumberArgument {
  private final long value;

  public LongArgument() {
    this(0L);
  }

  public LongArgument(long value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 40;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new LongArgument(((Number)obj).longValue());
    }
    throw new InvalidExpressionException(obj.getClass().getSimpleName() + " must of type Number");
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    if (expression.length() < 2) {
      return null;
    }
    for (int i = 0; i < expression.length() - 1; i++) {
      if (!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    if (expression.charAt(expression.length() - 1) == 'l'
        || expression.charAt(expression.length() - 1) == 'L') {
      return new LongArgument(Long.valueOf(expression.substring(0, expression.length() - 1)));
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
    return new LongArgument(0 - this.value);
  }
}
