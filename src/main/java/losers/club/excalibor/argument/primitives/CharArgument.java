package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class CharArgument implements NumberArgument {
  private static final MethodList methods = new MethodList(CharArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final char value;

  public CharArgument() {
    this.value = Character.MIN_VALUE;
  }

  public CharArgument(char value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument parse(String expression) {
    if (expression.startsWith("'") && expression.endsWith("'") && expression.length() == 3) {
      return new CharArgument(expression.charAt(1));
    }
    return null;
  }

  @Override
  public CharArgument add(Argument rhs) {
    return new CharArgument((char)(this.value + getRhsValue("+", rhs)));
  }


  @Override
  public Argument subtract(Argument rhs) {
    return new CharArgument((char)(this.value - getRhsValue("-", rhs)));
  }

  @Override
  public Argument multiply(Argument rhs) {
    return new CharArgument((char)(this.value * getRhsValue("*", rhs)));
  }

  @Override
  public Argument divide(Argument rhs) {
    return new CharArgument((char)(this.value / getRhsValue("/", rhs)));
  }

  @Override
  public Argument modulo(Argument rhs) {
    return new CharArgument((char)(this.value % getRhsValue("%", rhs)));
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

  private char getRhsValue(String op, Argument rhs) {
    if (rhs.getValue() instanceof Character) {
      return (char)rhs.getValue();
    }
    throw new IllegalArgumentException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        Character.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

}
