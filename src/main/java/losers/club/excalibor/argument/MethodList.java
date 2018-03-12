package losers.club.excalibor.argument;

import java.util.HashMap;

import losers.club.excalibor.operator.Operator;

public class MethodList {
  private volatile HashMap<Class<? extends Operator>, Method<?>> methods = null;
  
  private static HashMap<Class<? extends Argument>, MethodList> allLists =
      new HashMap<Class<? extends Argument>, MethodList>();
  
  public static Method<?> getMethod(Class<? extends Argument> arg, Class<? extends Operator> op) {
    MethodList list;
    if ((list = allLists.get(arg)) == null) {
      throw new RuntimeException("No MethodList exists for " + arg.getName());
    }
    return list.getMethod(op);
  }
  
  public static boolean isSupported(Class<? extends Argument> arg, Class<? extends Operator> op) {
    MethodList list;
    if ((list = allLists.get(arg)) == null) {
      throw new RuntimeException("No MethodList exists for " + arg.getName());
    }
    return list.isSupported(op);
  }
  
  public MethodList(Class<? extends Argument> arg) {
    this.methods = new HashMap<Class<? extends Operator>, Method<?>>();
    synchronized (allLists) {
      allLists.put(arg, this);
    }
  }
  
  
  public synchronized <T extends Argument> void add(Method<T> method) {
    // TODO
  }
  
  // TODO: Contains check/throws?
  public Method<?> getMethod(Class<? extends Operator> op) {
    return this.methods.get(op);
  }
  
  public boolean isSupported(Class<? extends Operator> op) {
    return this.methods.containsKey(op);
  }
}
