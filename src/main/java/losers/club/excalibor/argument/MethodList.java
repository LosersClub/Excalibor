package losers.club.excalibor.argument;

import java.util.HashMap;

import losers.club.excalibor.operator.Operator;

public class MethodList {
  private volatile HashMap<Class<? extends Operator>, Method<?>> methods = null;
  
  private static HashMap<Class<? extends Argument>, MethodList> allLists =
      new HashMap<Class<? extends Argument>, MethodList>();
  
  public static Method<?> getMethod(Class<? extends Argument> arg, Class<? extends Operator> op) {
    return getList(arg).getMethod(op);
  }
  
  public static boolean isSupported(Class<? extends Argument> arg, Class<? extends Operator> op) {
    return getList(arg).isSupported(op);
  }
  
  public static void register(Class<? extends Argument> arg, Class<? extends Operator> op,
      Method<?> method) {
    getList(arg).add(op, method);
  }
  
  private static MethodList getList(Class<? extends Argument> arg) {
    MethodList list;
    if ((list = allLists.get(arg)) == null) {
      throw new RuntimeException("No MethodList exists for " + arg.getName());
    }
    return list;
  }
  
  public MethodList(Class<? extends Argument> arg) {
    this.methods = new HashMap<Class<? extends Operator>, Method<?>>();
    synchronized (allLists) {
      allLists.put(arg, this);
    }
    
    // TODO: validate arg class here
  }
  
  
  public synchronized void add(Class<? extends Operator> op,
      Method<?> method) {
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
