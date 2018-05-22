package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.NumberArgument;

public class ShortArgument extends NumberArgument {
  private final short value;

  public ShortArgument() {
    this((short)0);
  }

  public ShortArgument(short value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 20;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new ShortArgument(((Number)obj).shortValue());
    }
    throw new IllegalArgumentException(obj.getClass().getSimpleName() + " must of type Number");
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
    if (expression.charAt(expression.length() - 1) == 's'
        || expression.charAt(expression.length() - 1) == 'S') {
      try {
        return new ShortArgument(Short.valueOf(expression.substring(0, expression.length() - 1)));
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
    return new ShortArgument((short)(0 - this.value));
  }
}
