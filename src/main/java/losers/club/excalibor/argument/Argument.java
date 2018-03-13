package losers.club.excalibor.argument;

import losers.club.excalibor.operator.Operator;

public interface Argument {

  Argument parse(String expression);

  Object getValue();

  default Method<? extends Argument> getMethod(Operator op) {
    return getMethod(op.getClass());
  }

  default Method<? extends Argument> getMethod(Class<? extends Operator> opClass) {
    return MethodList.getMethod(this.getClass(), opClass);
  }

  default boolean isSupported(Operator op) {
    return isSupported(op.getClass());
  }

  default boolean isSupported(Class<? extends Operator> opClass) {
    return MethodList.isSupported(this.getClass(), opClass);
  }
}
