package losers.club.excalibor.argument;

// Rough example of argument implementation?
public class MinusArgument implements Argument {
  private static final MethodList methods = new MethodList(MinusArgument.class);
  
  public static MethodList getMethodList() {
    return methods;
  }

  @Override
  public Argument parse(String expression) {
    // TODO Auto-generated method stub
    return null;
  }
}
