package losers.club.excalibor;

import java.util.HashMap;
import java.util.Map;

public class Expression {
  private final EvalTree tree;
  private final Map<String, Object> variables;

  public Expression(EvalTree tree) {
    this.tree = tree;
    this.variables = new HashMap<String, Object>();
  }
  
  public Object evaluate() {
    return null;
  }
  
  // Forces return type to be typeOf?
  public <T> T evaluate(Class<? extends T> typeOf) {
    return typeOf.cast(null);
  }
  
  // Sets a variable to a value. Throw if variable doesnt exist
  public void setVariable(String name, Object value) {
    
  }
  
  public void precompute() {
    // TODO: Attempt to collapse parts of tree that can be evaluated now
  }
}
