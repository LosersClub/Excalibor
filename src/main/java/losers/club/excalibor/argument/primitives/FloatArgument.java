package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class FloatArgument implements NumberArgument {
  private static final MethodList methods = new MethodList(FloatArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final float value;

  public FloatArgument() {
    this(0.0f);
  }

  public FloatArgument(float value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    return new FloatArgument((float)obj);
  }

  @Override
  public Argument parse(String expression) {
    // Char by char parsing because Kyle hates Regex
    int decimalCount = 0;
    for (int i = 0; i < expression.length() - 1; i++) {
      if (expression.charAt(i) == '.') {
        decimalCount++;
      } else if(!Character.isDigit(expression.charAt(i))) {
        return null;
      }
    }
    if (decimalCount == 1 &&
        (expression.charAt(expression.length() - 1) == 'f'
          || expression.charAt(expression.length() - 1) == 'F')) {
      return new FloatArgument(Float.valueOf(expression));
    }
    return null;
  }

  @Override
  public FloatArgument add(Argument rhs) {
    return new FloatArgument((float)(this.value + getRhsValue("+", rhs)));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new FloatArgument((float)(this.value - getRhsValue("-", rhs)));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new FloatArgument((float)(this.value * getRhsValue("*", rhs)));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new FloatArgument((float)(this.value / getRhsValue("/", rhs)));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new FloatArgument((float)(this.value % getRhsValue("%", rhs)));
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
        Float.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

  @Override
  public double getMathTypeValue() {
    return this.value;
  }

  @Override
  public Argument negate() {
    return new FloatArgument(0 - this.value);
  }
}
