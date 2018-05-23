package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;

public class ByteArgument extends NumberArgument {
  private final byte value;

  public ByteArgument() {
    this((byte)0);
  }

  public ByteArgument(byte value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 0;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new ByteArgument(((Number)obj).byteValue());
    }
    throw new IllegalArgumentException(obj.getClass().getSimpleName() + " must of type Number");
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    for (int i = 0; i < expression.length() - 1; i++) {
      if (!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    if (expression.length() >= 2 && (expression.charAt(expression.length() - 1) == 'b' ||
        expression.charAt(expression.length() - 1) == 'B')) {
      try {
        byte temp = Byte.valueOf(expression.substring(0, expression.length() - 1));
        return new ByteArgument(temp);
      } catch (NumberFormatException e) {
        return null;
      }
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
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(this.value == getRhsValue("==", rhs));
  }

  @Override
  public double getMathTypeValue() {
    return this.value;
  }

  @Override
  public Argument negate() {
    return new ByteArgument((byte)(0 - this.value));
  }
}
