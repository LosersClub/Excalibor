package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NumberArgument;

public class CharArgument extends NumberArgument {
  private final char value;

  public CharArgument() {
    this('\u0000');
  }

  public CharArgument(char value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 10;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new CharArgument((char)((Number)obj).shortValue());
    }
    return new CharArgument((char)obj);
  }

  @Override
  public Argument parse(String expression) {
    if (expression.startsWith("'") && expression.endsWith("'")) {
      if (expression.charAt(1) != '\\' && expression.length() == 3) {
          return new CharArgument(expression.charAt(1));
      // Handles the special unicode case: e/x \u0000
      } else if (expression.length() == 8) {
        try {
          char result = (char)Integer.parseInt(expression.substring(3, 7), 16);
          return new CharArgument(result);
        } catch (NumberFormatException e) {
          return null;
        }
      } else if (expression.length() == 4) {
        return new CharArgument(evaluateEscapeChar(expression.substring(1, 3)));
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
    return new CharArgument((char)(0 - this.value));
  }

  private char evaluateEscapeChar(String s) {
    switch(s) {
      case "\\t":
        return '\t';
      case "\\n":
        return '\n';
      case "\\b":
        return '\b';
      case "\\r":
        return '\r';
      case "\\f":
        return '\f';
      case "\\'":
        return '\'';
      case "\\\"":
        return '\"';
      case "\\\\":
        return '\\';
    }
    throw new IllegalArgumentException("Unknown escape sequence: \"'" + s + "'\"");
  }
}
