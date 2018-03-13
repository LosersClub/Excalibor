package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.MethodList;

public class BooleanArgument implements LogicalArgument {
  private static final MethodList methods = new MethodList(BooleanArgument.class);

  public static MethodList getMethodList() {
    return methods;
  }

  private boolean value;

  public BooleanArgument() {
    this.value = false;
  }

  public BooleanArgument(boolean value) {
    this.value = value;
  }

  public BooleanArgument not() {
    return null;
  }

  @Override
  public Argument parse(String expression) {
    expression = expression.toLowerCase();
    if (expression.equals("true") || expression.equals("false")) {
      return new BooleanArgument(Boolean.valueOf(expression));
    }
    return null;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public BooleanArgument equals(Argument rhs) {
    if (rhs.getValue() instanceof Boolean) {
      return new BooleanArgument(this.value == (boolean)rhs.getValue());
    }
    throw new IllegalArgumentException(getExceptionString("==", rhs));
  }

  @Override
  public BooleanArgument and(Argument rhs) {
    if (rhs.getValue() instanceof Boolean) {
      return new BooleanArgument(this.value && (boolean)rhs.getValue());
    }
    throw new IllegalArgumentException(getExceptionString("&&", rhs));
  }

  @Override
  public BooleanArgument or(Argument rhs) {
    if (rhs.getValue() instanceof Boolean) {
      return new BooleanArgument(this.value || (boolean)rhs.getValue());
    }
    throw new IllegalArgumentException(getExceptionString("||", rhs));
  }

  @Override
  public BooleanArgument xor(Argument rhs) {
    if (rhs.getValue() instanceof Boolean) {
      return new BooleanArgument(this.value ^ (boolean)rhs.getValue());
    }
    throw new IllegalArgumentException(getExceptionString("^ [xor]", rhs));
  }

  private String getExceptionString(String op, Argument rhs) {
    return String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.value,
        Integer.class.getName(), rhs.getValue().toString(), rhs.getValue().getClass().getName());
  }

}
