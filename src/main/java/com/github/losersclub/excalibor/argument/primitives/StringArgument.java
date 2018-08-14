package com.github.losersclub.excalibor.argument.primitives;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.ComparableArgument;

public class StringArgument extends ComparableArgument {
  private final String value;

  public StringArgument() {
    this("");
  }

  public StringArgument(String value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument build(Object obj) {
    return new StringArgument((String)obj);
  }

  @Override
  public Argument parse(String expression) {
    if(expression.startsWith("\"") && expression.endsWith("\"")) {
      for (int i = 0; i < expression.length(); i++) {
        if (expression.charAt(i) == '\\') {
          expression = expression.substring(0, i) +
            evaluateEscapeChar(expression.charAt(i + 1)) +
            expression.substring(i + 2, expression.length());
        }
      }
      return new StringArgument(expression.substring(1, expression.length()-1));
    }
    return null;
  }
  
  @Override
  public String toString() {
    return '"' + this.value + '"';
  }

  @Override
  public BooleanArgument lessThan(Argument rhs) {
    return new BooleanArgument(this.value.compareTo(getRhsValue("<", rhs)) < 0);
  }

  @Override
  public BooleanArgument greaterThan(Argument rhs) {
    return new BooleanArgument(this.value.compareTo(getRhsValue(">", rhs)) > 0);
  }

  @Override
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(this.value.compareTo(getRhsValue("==", rhs)) == 0);
  }

  public StringArgument concat(Argument rhs) {
    return new StringArgument(this.value + getRhsValue("+", rhs));
  }

  private String getRhsValue(String op, Argument rhs) {
    if (rhs.getValue() instanceof String) {
      return (String)rhs.getValue();
    }
    throw new InvalidExpressionException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        String.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

  private String evaluateEscapeChar(char c) {
    switch(c) {
      case 't':
        return "\t";
      case 'n':
        return "\n";
      case 'b':
        return "\b";
      case 'r':
        return "\r";
      case 'f':
        return "\f";
      case '\'':
        return "\'";
      case '\"':
        return "\"";
      case '\\':
        return "\\";
    }
    throw new InvalidExpressionException("Unknown escape sequence: \"\\" + c + "\"");
  }

}
