package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public class IntArgument extends NumberArgument {
  private final int value;

  public IntArgument() {
    this(0);
  }

  public IntArgument(int value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 30;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new IntArgument(((Number)obj).intValue());
    }
    throw new IllegalArgumentException(obj.getClass().getSimpleName() + " must of type Number");
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    if (expression.length() < 1) {
      return null;
    }
    for (int i = 0; i < expression.length(); i++) {
      if (!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    try {
      return new IntArgument(Integer.valueOf(expression));
    } catch (NumberFormatException e) {
      return null;
    }
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
    return new IntArgument(0 - this.value);
  }

}
