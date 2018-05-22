package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

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
    return 1;
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
      }
    }
    return null;
  }
  
  @Override
  double add(double rhs) {
    return this.value + rhs;
  }
  
  @Override
  double subtract(double rhs) {
    return this.value - rhs;
  }
  
  @Override
  double multiply(double rhs) {
    return this.value * rhs;
  }
  
  @Override
  double divide(double rhs) {
    return this.value / rhs;
  }
  
  @Override
  double modulo(double rhs) {
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

}
