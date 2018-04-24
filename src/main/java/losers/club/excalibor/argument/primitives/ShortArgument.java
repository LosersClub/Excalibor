package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class ShortArgument implements NumberArgument {
  private static final MethodList methods = new MethodList(ShortArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final short value;

  public ShortArgument() {
    this((short)0);
  }

  public ShortArgument(short value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
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
  public ShortArgument add(Argument rhs) {
    return new ShortArgument((short)(this.value + getRhsValue("+", rhs)));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new ShortArgument((short)(this.value - getRhsValue("-", rhs)));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new ShortArgument((short)(this.value * getRhsValue("*", rhs)));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new ShortArgument((short)(this.value / getRhsValue("/", rhs)));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new ShortArgument((short)(this.value % getRhsValue("%", rhs)));
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
        Short.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
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
