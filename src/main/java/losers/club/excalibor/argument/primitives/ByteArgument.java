package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class ByteArgument implements NumberArgument {
  private static final MethodList methods = new MethodList(ByteArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final byte value;

  public ByteArgument() {
    this((byte)0);
  }

  public ByteArgument(byte value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    for (int i = 0; i < expression.length() - 1; i++) {
      if (!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    if (expression.charAt(expression.length() - 1) == 'b' ||
        expression.charAt(expression.length() - 1) == 'B') {
      return new ByteArgument(Byte.valueOf(expression.substring(0, expression.length() - 1)));
    }
    return null;
  }

  @Override
  public ByteArgument add(Argument rhs) {
    return new ByteArgument((byte)(this.value + getRhsValue("+", rhs)));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new ByteArgument((byte)(this.value - getRhsValue("-", rhs)));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new ByteArgument((byte)(this.value * getRhsValue("*", rhs)));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new ByteArgument((byte)(this.value / getRhsValue("/", rhs)));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new ByteArgument((byte)(this.value % getRhsValue("%", rhs)));
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
    if (rhs.getValue() instanceof Number) {
      return (double)rhs.getValue();
    }
    throw new IllegalArgumentException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        Byte.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }
}
