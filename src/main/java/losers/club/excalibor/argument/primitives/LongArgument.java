package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public class LongArgument implements NumberArgument {
  private final long value;

  public LongArgument() {
    this(0L);
  }

  public LongArgument(long value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    return new LongArgument((long)obj);
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
    if (expression.charAt(expression.length() - 1) == 'l'
        || expression.charAt(expression.length() - 1) == 'L') {
      return new LongArgument(Long.valueOf(expression.substring(0, expression.length() - 1)));
    }
    return null;
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
    if (rhs instanceof NumberArgument) {
      return ((NumberArgument)(rhs)).getMathTypeValue();
    }
    throw new IllegalArgumentException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        Long.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

  @Override
  public double getMathTypeValue() {
    return this.value;
  }

  @Override
  public Argument negate() {
    return new LongArgument(0 - this.value);
  }
}
