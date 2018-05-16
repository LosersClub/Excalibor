package losers.club.excalibor.argument;

import losers.club.excalibor.operator.Operator;

public interface Argument {

  Argument parse(String expression);
  
  Argument build(Object obj);

  Object getValue();
  
  default Argument convert(VariableArgument vArg) {
    if (vArg.getValue() == null || vArg.getValue().getClass() != this.getValue().getClass()) {
      return null;
    }
    return build(vArg.getValue());
  }

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
