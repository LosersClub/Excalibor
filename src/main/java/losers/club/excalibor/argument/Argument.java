package losers.club.excalibor.argument;

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
}
