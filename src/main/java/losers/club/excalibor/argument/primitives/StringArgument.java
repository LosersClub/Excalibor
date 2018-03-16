package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class StringArgument implements ComparableArgument{
  private static final MethodList methods = new MethodList(StringArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private final String value;

  public StringArgument() {
    this("");
  }

  public StringArgument(String value) {
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Argument parse(String expression) {
    if(expression.startsWith("\"") && expression.endsWith("\"")) {
      return new StringArgument(expression.substring(1, expression.length()-1));
    }
    return null;
  }

  @Override
  public BooleanArgument lessThan(Argument rhs) {
    return new BooleanArgument(this.value.compareTo(getRhsValue("<", rhs)) < 0);
  }

  @Override
  public BooleanArgument greaterThan(Argument rhs) {
    return new BooleanArgument(this.value.compareTo(getRhsValue(">", rhs)) > 0);
  }

  @Override
  public BooleanArgument equals(Argument rhs) {
    return new BooleanArgument(this.value.compareTo(getRhsValue("==", rhs)) == 0);
  }

  public StringArgument concat(Argument rhs) {
    return new StringArgument(this.value + getRhsValue("+", rhs));
  }

  private String getRhsValue(String op, Argument rhs) {
    if (rhs.getValue() instanceof String) {
      return (String)rhs.getValue();
    }
    throw new IllegalArgumentException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        String.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName()));
  }

}
