package losers.club.excalibor;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.NotEvaluatable;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;

public class EvalTree {
  Node root = null;
  private int size = 0;
  
  public EvalTree() {
    this.root = new Node();
  }
  
  public boolean valid() {
    Node temp = this.root;
    while (temp.right != null) {
      temp = temp.right;
    }
    return this.root.isArg() || temp.isArg();
  }
  
  public int size() {
    return this.size;
  }
  
  public void insert(Argument arg) {
    this.insert(arg, null);
  }
  
  public void insert(Argument arg, UnaryOperator uOp) {
    Node temp = this.root;
    while (temp.right != null) {
      temp = temp.right;
    }
    
    if (temp.isArg()) {
      throw new IllegalArgumentException("An operator must be added between two arguments");
    }
    
    size += 1;
    if (temp.isEmpty()) {
      temp.value = arg;
      temp.uOp = uOp != null ? uOp : temp.uOp;
      return;
    }
    
    Node node = new Node();
    node.value = arg;
    node.uOp = uOp;
    temp.right = node;
  }
  
  public void insert(EvalTree tree) {
    if (!tree.valid()) {
      throw new IllegalArgumentException("The tree being inserted must be valid");
    }
    
    Node temp = this.root;
    while (temp.right != null) {
      temp = temp.right;
    }
    if (temp.isArg()) {
      throw new IllegalArgumentException("An operator must be added between two arguments");
    }
    size += tree.size;
    if (temp.isEmpty()) {
      temp.op = tree.root.op;
      temp.value = tree.root.value;
      temp.uOp = temp.uOp != null ? temp.uOp : tree.root.uOp;
      temp.left = tree.root.left;
      temp.right = tree.root.right;
      tree.root = null;
      temp.validStruct = true;
      return;
    }
    temp.right = tree.root;
    temp.right.validStruct = true;
    tree.root = null;
  }
  
  public void insert(Operator operator) {    
    if (this.root.isEmpty() || (this.root.isOp() && this.root.right == null)) {
      if (operator instanceof UnaryOperator) {
        if (this.root.isEmpty()) {
          this.root.uOp = (UnaryOperator)operator;
        } else {
          this.root.right = new Node();
          this.root.right.uOp = (UnaryOperator)operator;
        }
        return;
      }
      
      throw new IllegalArgumentException("No right-hand argument set (double non-unary operators"
          + " is not allowed)");
    }
    
    size += 1;
    Node node = new Node();
    node.op = operator;
    Node prev = this.root;
    Node current = this.root;
    while (!current.validStruct && !current.isArg() && node.op.priority() > current.op.priority()) {
      node.left = current.right;
      current.right = node;
      if (prev != current) {
        prev.right = current;
      }
      prev = current;
      current = node.left;
    }
    node.left = current;
    this.root = this.root == current ? node : this.root;
  }
  
  public Argument evaluate() throws NotEvaluatableException {
    if (!this.valid()) {
      throw new NotEvaluatableException("EvalTree is not currently valid.");
    }
    this.evaluate(this.root);
    return this.root.value;
  }
  
  private void evaluate(Node node) {
    if (node == null) {
      return;
    }
    evaluate(node.left);
    evaluate(node.right);
    if (node.isOp()) {
      if (node.left.value instanceof NotEvaluatable &&
          !((NotEvaluatable)node.left.value).isEvaluatable()) {
        throw new NotEvaluatableException(String.format("Unable to evaluate %s", node.left.value));
      }
      if (node.right.value instanceof NotEvaluatable &&
          !((NotEvaluatable)node.right.value).isEvaluatable()) {
        throw new NotEvaluatableException(String.format("Unable to evaluate %s", node.right.value));
      }
      node.value = node.op.evaluate(node.left.value, node.right.value);
    }
    
    if (node.hasUnaryOp()) {
      if (node.value instanceof NotEvaluatable && !((NotEvaluatable)node.value).isEvaluatable()) {
        throw new NotEvaluatableException(String.format("Unable to evaluate %s",  node.value));
      }
      node.value = node.uOp.evaluate(node.value);
    }
  }
  
  static class Node {
    Node left = null;
    Node right = null;
    
    Argument value = null;
    Operator op = null;
    UnaryOperator uOp = null;
    boolean validStruct = false;
    
    boolean isEmpty() {
      return value == null && op == null;
    }
    
    boolean isArg() {
      return value != null;
    }
    
    boolean isOp() {
      return op != null;
    }
    
    boolean hasUnaryOp() {
      return uOp != null;
    }
  }
}