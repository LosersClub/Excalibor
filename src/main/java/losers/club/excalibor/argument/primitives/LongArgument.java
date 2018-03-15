package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class LongArgument implements NumberArgument {
  private static final MethodList methods = new MethodList(LongArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final long value;

  public LongArgument() {
    this(Long.MIN_VALUE);
  }

  public LongArgument(long value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    for (int i = 0; i < expression.length(); i++) {
      if(!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    return new LongArgument(Long.valueOf(expression));
  }

  @Override
  public LongArgument add(Argument rhs) {
    return new LongArgument((long)(this.value + getRhsValue("+", rhs)));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new LongArgument((long)(this.value - getRhsValue("-", rhs)));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new LongArgument((long)(this.value * getRhsValue("*", rhs)));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new LongArgument((long)(this.value / getRhsValue("/", rhs)));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new LongArgument((long)(this.value % getRhsValue("%", rhs)));
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
        Long.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }
}
