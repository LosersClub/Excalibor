package losers.club.excalibor.operator;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.Method;
import losers.club.excalibor.argument.MethodList;

public abstract class Operator {
  
  public abstract String getSymbol();
  
  public abstract int priority();
  
  public abstract Argument evaluate(Argument lhs, Argument rhs);
  
  public final void registerMethod(Class<? extends Argument> arg, Method<?> method) {
    MethodList.register(arg, this.getClass(), method);
  }
  
  @Override
  public int hashCode() {
    return getSymbol().hashCode();
  }
}
