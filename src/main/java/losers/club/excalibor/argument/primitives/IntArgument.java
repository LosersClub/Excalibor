package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public class IntArgument implements NumberArgument{
  private final int value;

  public IntArgument() {
    this(0);
  }

  public IntArgument(int value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    return new IntArgument((int)obj);
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
  public IntArgument add(Argument rhs) {
    return new IntArgument((int)(this.value + getRhsValue("+", rhs)));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new IntArgument((int)(this.value - getRhsValue("-", rhs)));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new IntArgument((int)(this.value * getRhsValue("*", rhs)));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new IntArgument((int)(this.value / getRhsValue("/", rhs)));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new IntArgument((int)(this.value % getRhsValue("%", rhs)));
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

  private double getRhsValue(String op, Argument rhs) {
    if (rhs instanceof NumberArgument) {
      return ((NumberArgument)(rhs)).getMathTypeValue();
    }
    throw new IllegalArgumentException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        Integer.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
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
