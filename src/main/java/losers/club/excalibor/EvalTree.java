package losers.club.excalibor;

import java.util.Stack;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.NotEvaluatable;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;

public class EvalTree {
  Node root = null; // TODO: make private/give smarter access?
  private Stack<Bracket> currentStack = new Stack<Bracket>(); // TODO: Custom stack structure
  private int size = 0;
  
  public EvalTree() {
    this.root = new Node();
    currentStack.push(new Bracket(null, this.root));
  }
  
  public boolean valid() {
    return this.currentStack.size() == 1 && this.currentStack.peek().count == 0 &&
        (this.current().isEmpty() || this.current().isArg() || this.current().right != null);
  }
  
  public int size() {
    return this.size;
  }
  
  public void insert(Argument arg) {
    this.insert(arg, null);
  }
  
  public void insert(Argument arg, UnaryOperator uOp) {
    Node temp = this.current();
    while (temp.right != null) {
      temp = temp.right;
    }
    
    if (temp.isArg()) {
      throw new IllegalArgumentException("An operator must be added between to arguments");
    }
    
    size += 1;
    // First argument to tree (expression with no arg).
    if (temp.isEmpty()) {
      temp.value = arg;
      temp.op = uOp;
      return;
    }
    
    Node node = new Node();
    node.value = arg;
    node.op = uOp;
    temp.right = node;
  }
  
  public void insert(Operator operator) {
    if (this.current().isEmpty() || (this.current().isOp() && !this.current().isArg() &&
        this.current().right == null)) {
      throw new IllegalArgumentException("No right-hand argument set (double non-unary operators"
          + " is not allowed)");
    }
    
    size += 1;
    Node node = new Node();
    node.op = operator;
    if (this.current().isArg() || node.op.priority() <= this.current().op.priority()) {
      node.left = this.current();
      this.updateCurrent(node);
    } else {
      node.left = this.current().right;
      current().right = node;
    }
  }
  
  public Argument evaluate() throws NotEvaluatableException {
    if (!this.valid()) {
      throw new NotEvaluatableException("EvalTree is not currently valid.");
    }
    this.evaluate(root);
    return this.root.value;
  }
  
  private void evaluate(Node node) {
    if (!node.isOp()) {
      return;
    }
    if (node.isArg()) {
      node.value = ((UnaryOperator)node.op).evaluate(node.value);
      return;
    }
    if (node.left.isOp()) {
      evaluate(node.left);
    }
    if (node.right.isOp()) {
      evaluate(node.right);
    }
    if (node.left.value instanceof NotEvaluatable &&
        !((NotEvaluatable)node.left.value).isEvaluatable()) {
      throw new NotEvaluatableException(String.format("Unable to evaluate %s",  node.left.value));
    }
    if (node.right.value instanceof NotEvaluatable &&
        !((NotEvaluatable)node.right.value).isEvaluatable()) {
      throw new NotEvaluatableException(String.format("Unable to evaluate %s",  node.right.value));
    }
    node.value = node.op.evaluate(node.left.value, node.right.value);
  }
  
  public void openBracket() {
    Node temp = this.current();
    while (temp.right != null) {
      temp = temp.right;
    }
    
    if (temp.isArg()) {
      throw new UnsupportedOperationException("An open bracket must be preceeded by nothing or"
          + " follow an operator");
    }
    if (temp.isEmpty()) {
      this.currentStack.peek().count += 1;
      return;
    }
    temp.right = new Node();
    this.currentStack.push(new Bracket(temp, temp.right));
  }
  
  public void closeBracket() {
    if (this.currentStack.peek().count == 0 && this.currentStack.size() == 1) {
      throw new UnsupportedOperationException("An open bracket must be added before a"
          + " close bracket");
    }
    
    if (this.currentStack.peek().count > 0) {
      this.currentStack.peek().count -= 1;
      return;
    }
    this.currentStack.pop();
  }
  
  private Node current() {
    return currentStack.peek().node;
  }
  
  private void updateCurrent(Node node) {
    this.currentStack.peek().node = node;
    if (this.currentStack.size() == 1) {
      this.root = node;
      return;
    }
    // Update parents's right child
    this.currentStack.peek().parent.right = node;
  }
  
  private static class Bracket {
    final Node parent;
    Node node = null;
    int count = 0;
    
    Bracket(Node parent, Node node) {
      this.parent = parent;
      this.node = node;
    }
  }
  
  static class Node {
    Node left = null;
    Node right = null;
    
    Argument value = null;
    Operator op = null;
    
    boolean isEmpty() {
      return value == null && op == null;
    }
    
    boolean isArg() {
      return value != null;
    }
    
    boolean isOp() {
      return op != null;
    }
  }
}