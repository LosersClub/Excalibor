package losers.club.excalibor;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.NotEvaluable;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;

public class EvalTree {
  private Node root = null;
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
  
  public Argument evaluate() throws NotEvaluableException {
    if (!this.valid()) {
      throw new NotEvaluableException("EvalTree is not currently valid.");
    }
    this.evaluate(this.root, true);
    return this.root.value;
  }
  
  public boolean precompute() {
    if (!this.valid()) {
      return false;
    }
    this.evaluate(this.root, false);
    return this.root.isArg();
  }
  
  private void evaluate(Node node, boolean doThrow) {
    if (node == null) {
      return;
    }
    evaluate(node.left, doThrow);
    evaluate(node.right, doThrow);
    node.evaluate(doThrow);
  }
  
  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that instanceof Node) {
      return equals(this.root, (Node)that);
    }
    if (that instanceof EvalTree) {
      return equals(this.root, ((EvalTree)that).root);
    }
    return false;
  }
  
  private static boolean equals(Node leftTree, Node rightTree) {
    if (leftTree == null || rightTree == null) {
      return leftTree == null && rightTree == null;
    }
    
    if (leftTree.op != rightTree.op || leftTree.value != rightTree.value ||
        leftTree.uOp != rightTree.uOp) {
      return false;
    }
    
    return equals(leftTree.left, rightTree.left) && equals(leftTree.right, rightTree.right);
  }
  
  static final class Node {
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
    
    void evaluate(boolean doThrow) {
      if (this.isOp()) {
        if (this.left.value instanceof NotEvaluable &&
            !((NotEvaluable)this.left.value).isEvaluable()) {
          if (!doThrow) {
            return;
          }
          throw new NotEvaluableException(String.format("Unable to evaluate %s",
              this.left.value));
        }
        if (this.right.value instanceof NotEvaluable &&
            !((NotEvaluable)this.right.value).isEvaluable()) {
          if (!doThrow) {
            return;
          }
          throw new NotEvaluableException(String.format("Unable to evaluate %s",
              this.right.value));
        }
        this.value = this.op.evaluate(this.left.value, this.right.value);
      }
      
      if (this.hasUnaryOp()) {
        if (this.value instanceof NotEvaluable && !((NotEvaluable)this.value).isEvaluable()) {
          if (!doThrow) {
            return;
          }
          throw new NotEvaluableException(String.format("Unable to evaluate %s",  this.value));
        }
        this.value = this.uOp.evaluate(this.value);
      }
      
      if (this.isArg()) {
        if (this.value instanceof NotEvaluable && !((NotEvaluable)this.value).isEvaluable()) {
          if (!doThrow) {
            return;
          }
          throw new NotEvaluableException(String.format("Unable to evaluate %s",  this.value));
        }
      }
    }
  }
}