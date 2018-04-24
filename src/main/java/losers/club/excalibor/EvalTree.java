package losers.club.excalibor;

import java.util.Stack;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;

public class EvalTree {
  // TODO: replace root with first value in stack?
  Node root = null; // TODO: make private/give smarter access?
  private Stack<Bracket> currentStack = new Stack<Bracket>(); // TODO: Custom stack structure
  private int size = 0;
  
  public EvalTree() {
    this.root = new Node();
    currentStack.push(new Bracket(this.root));
  }
  
  public boolean valid() {
    return this.currentStack.size() == 1 && this.currentStack.peek().count == 0 &&
        (this.current().isArg() || this.current().right != null);
  }
  
  public int size() {
    return this.size;
  }
  
  public void insert(Argument arg) {
    this.insert(arg, null);
  }
  
  public void insert(Argument arg, UnaryOperator uOp) {
    if (this.current().isArg() || (this.current().isOp() && this.current().right != null)) {
      throw new IllegalArgumentException("An operator must be added between to arguments");
    }
    size += 1;
    // First argument to tree (expression with no arg).
    if (this.current().isEmpty()) {
      this.current().value = arg;
      this.current().op = uOp;
      return;
    }
    
    Node node = new Node();
    node.value = arg;
    node.op = uOp;
    this.current().right = node;
  }
  
  public void insert(Operator operator) {
    if (this.current().isOp() && !this.current().isUnary() && this.current().right == null) {
      throw new IllegalArgumentException("No right-hand argument set (double non-unary operators"
          + " is not allowed)");
    }
    Node node = new Node();
    node.left = this.current();
    node.op = operator;
    this.updateCurrent(node);
    size += 1;
  }
  
  public void openBracket() {
    if (this.current().isArg()) {
      throw new UnsupportedOperationException("An open bracket must be preceeded by nothing or"
          + " follow an operator");
    }
    if (this.current().isEmpty()) {
      this.currentStack.peek().count += 1;
      return;
    }
    
    if (this.current().right == null) {
      this.current().right = new Node();
      this.currentStack.push(new Bracket(this.current().right));
    }
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
  
  public void heapify() {
    if (!this.valid()) {
      throw new RuntimeException("Tree must be valid to heapify it");
    }
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
    // Update previous current's right child
    Bracket top = this.currentStack.pop();
    this.currentStack.peek().node.right = node;
    this.currentStack.push(top);
  }
  
  private static class Bracket {
    Node node = null;
    int count = 0;
    
    Bracket(Node node) {
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
    
    boolean isUnary() {
      return op != null && value != null && op instanceof UnaryOperator;
    }
    
    boolean isOp() {
      return op != null;
    }
  }
}