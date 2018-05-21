package losers.club.excalibor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import losers.club.excalibor.EvalTree.Node;
import losers.club.excalibor.argument.NotEvaluable;
import losers.club.excalibor.argument.VariableArgument;

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
    if (this.tree.getRoot().isArg() && !(this.tree.getRoot().value instanceof NotEvaluable)) {
      return tree.getRoot().value.getValue();
    }
    EvalTree copy = new EvalTree(this.tree);
    return copy.evaluate().getValue();
  }
  
  public Object evaluate(Map<String, Object> vars) throws NotEvaluableException {
    vars.forEach(this::setVariable);
    return this.evaluate();
  }
  
  public <T> T evaluate(Class<? extends T> typeOf) throws NotEvaluableException {
    Object out = null;
    try {
      return typeOf.cast(out = this.evaluate());
    } catch (ClassCastException e) {
      throw new RuntimeException(String.format("This expression returns '%s' but '%s' was the "
          + "expected type", out.getClass(), typeOf));
    }
  }
  
  public void setVariable(String name, Object value) {
    if (!variables.containsKey(name)) {
      throw new RuntimeException(String.format("%s does not exist as a variable in this expression",
          name));
    }
    variables.get(name).setValue(value);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    toString(sb, tree.getRoot());
    return sb.toString();
  }
  
  private void toString(StringBuilder sb, Node node) {
    if (node == null) {
      return;
    }
    toString(sb, node.left);
    
    if (node.isArg()) {
      if (node.hasUnaryOp()) {
        sb.append(node.uOp.getSymbol());
      }
      Object val = node.value.getValue();
      if (node.value instanceof NotEvaluable) {
        for (Entry<String, VariableArgument> e : this.variables.entrySet()) {
          if (e.getValue() == node.value) {
            val = e.getKey();
            break;
          }
        }
      }
      sb.append(val);
    }
    if (node.isOp()) {
      sb.append(" " + node.op.getSymbol() + " ");
    }
    
    if (node.right != null && node.right.isOp()) {
      if (node.right.hasUnaryOp()) {
        sb.append(node.right.uOp.getSymbol());
      }
      sb.append("(");
    }
    toString(sb, node.right);
    if (node.right != null && node.right.isOp()) {
      sb.append(")");
    }
  }
}
