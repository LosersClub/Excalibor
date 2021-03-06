package com.github.losersclub.excalibor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.losersclub.excalibor.argument.NotEvaluable;
import com.github.losersclub.excalibor.argument.VariableArgument;

public class Expression {
  private final EvalTree tree;
  private final Map<String, VariableArgument> variables;

  public Expression(EvalTree tree) {
    this(tree, new HashMap<String, VariableArgument>());
  }
  
  public Expression(EvalTree tree,
      Map<String, VariableArgument> variables) {
    this.tree = tree;
    this.variables = variables;
  }
  
  public Object evaluate() throws NotEvaluableException {
    if (this.computed()) {
      return tree.getRoot().value.getValue();
    }
    EvalTree copy = new EvalTree(this.tree);
    return copy.evaluate().getValue();
  }
  
  public Object evaluate(Map<String, Object> vars) throws NotEvaluableException {
    vars.forEach(this::setVariable);
    return this.evaluate();
  }
  
  public <T> T evaluate(Map<String, Object> vars, Class<? extends T> typeOf)
      throws NotEvaluableException {
    vars.forEach(this::setVariable);
    return this.evaluate(typeOf);
  }
  
  public <T> T evaluate(Class<? extends T> typeOf) throws NotEvaluableException {
    Object out = null;
    try {
      return typeOf.cast(out = this.evaluate());
    } catch (ClassCastException e) {
      throw new ClassCastException(String.format("This expression returns '%s' but '%s' was the "
          + "expected type", out.getClass(), typeOf));
    }
  }
  
  public void setVariable(String name, Object value) {
    if (!variables.containsKey(name)) {
      throw new IllegalArgumentException(String.format("%s does not exist as a variable in this "
          + "expression", name));
    }
    variables.get(name).setValue(value);
  }
  
  public Collection<String> variables() {
    return variables.keySet();
  }
  
  public boolean computed() {
    return this.tree.getRoot().isArg() && !(this.tree.getRoot().value instanceof NotEvaluable);
  }
  
  @Override
  public String toString() {
    return this.tree.toString();
  }
}
