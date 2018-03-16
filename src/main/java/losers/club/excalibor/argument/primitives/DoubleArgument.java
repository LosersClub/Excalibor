package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class DoubleArgument implements NumberArgument{

  private static final MethodList methods = new MethodList(DoubleArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final double value;

  public DoubleArgument() {
    this(0.0);
  }

  public DoubleArgument(double value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    boolean decimalFound = false;
    for (int i = 0; i < expression.length(); i++) {
      if (expression.charAt(i) == '.') {
        decimalFound = true;
      } else if(!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    if (decimalFound) {
      return new DoubleArgument(Double.valueOf(expression));
    }
    return null;
  }

  @Override
  public DoubleArgument add(Argument rhs) {
    return new DoubleArgument(this.value + getRhsValue("+", rhs));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new DoubleArgument(this.value - getRhsValue("-", rhs));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new DoubleArgument(this.value * getRhsValue("*", rhs));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new DoubleArgument(this.value / getRhsValue("/", rhs));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new DoubleArgument(this.value % getRhsValue("%", rhs));
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
        Double.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

}