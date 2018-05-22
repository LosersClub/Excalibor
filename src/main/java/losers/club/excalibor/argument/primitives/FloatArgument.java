package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public class FloatArgument extends NumberArgument {
  private final float value;

  public FloatArgument() {
    this(0.0f);
  }

  public FloatArgument(float value) {
    this.value = value;
  }
  
  @Override
  public int priority() {
    return 50;
  }

  @Override
  public Object getValue() {
    return value;
  }
  
  @Override
  public Argument build(Object obj) {
    if (obj instanceof Number) {
      return new FloatArgument(((Number)obj).floatValue());
    }
    throw new IllegalArgumentException(obj.getClass().getSimpleName() + " must of type Number");
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
    return new FloatArgument(0 - this.value);
  }
}
