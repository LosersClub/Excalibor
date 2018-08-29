package com.github.losersclub.excalibor;

import java.util.Objects;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NotEvaluable;
import com.github.losersclub.excalibor.argument.VariableArgument;
import com.github.losersclub.excalibor.operator.Operator;
import com.github.losersclub.excalibor.operator.UnaryOperator;

public final class EvalTree {
  private Node root = null;
  private int size = 0;
  
  public EvalTree() {
    this.root = new Node();
  }
  
  public EvalTree(EvalTree other) {
    this.root = copy(other.root);
    this.size = other.size;
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
  
  Node getRoot() {
    return root;
  }
  
  public void insert(Argument arg) {
    Node temp = this.root;
    while (temp.right != null) {
      temp = temp.right;
    }
    
    if (temp.isArg()) {
      throw new InvalidExpressionException("Unable to compile the given expression. "
          + "A non-unary operator is needed between the current expression \"" + this.toString()
          + "\" and the argument \"" + arg.toString() + "\" in order for the expression to be "
          + "valid.");
    }
    
    size += 1;
    if (temp.isEmpty()) {
      temp.value = arg;
      return;
    }
    
    Node node = new Node();
    node.value = arg;
    temp.right = node;
  }
  
  public void insert(EvalTree tree) {
    if (!tree.valid()) {
      throw new InvalidExpressionException("The expression \"" + tree.toString() + "\" must be "
          + "non-empty and completely closed. That is, it must not contain an operator without"
          + "left-hand and right-hand arguments defined.");
    }
    
    Node temp = this.root;
    while (temp.right != null) {
      temp = temp.right;
    }
    if (temp.isArg()) {
      throw new InvalidExpressionException("Unable to compile the given expression. "
          + "A non-unary operator is needed between the current expression \"" + this.toString()
          + "\" and the expression \"" + tree + "\" in order for the expression to be valid.");
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
      throw new InvalidExpressionException("An argument must exist between two non-unary "
          + "operators. Unable to add the \"" + operator.getSymbol() + "\" operator to the "
          + (this.root.isEmpty() ? "start of an expression." : " end of the current expression: \""
          + this.toString() + "\""));
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
      throw new NotEvaluableException("The expression \"" + this.toString() + "\" must be "
          + "non-empty and completely closed before evaluating. That is, it must not contain an "
          + "operator without left-hand and right-hand arguments defined.");
    }
    this.evaluate(this.root, false);
    return this.root.value;
  }
  
  public boolean precompute() {
    if (!this.valid()) {
      return false;
    }
    this.evaluate(this.root, true);
    return this.root.isArg() && !(this.root.value instanceof NotEvaluable);
  }
  
  private void evaluate(Node node, boolean precompute) {
    if (node == null) {
      return;
    }
    evaluate(node.left, precompute);
    evaluate(node.right, precompute);
    if (node.evaluate(precompute)) {
      this.size -= 2;
    }
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
    
    if (leftTree.op != rightTree.op || leftTree.uOp != rightTree.uOp ||
        !Objects.equals(leftTree.value, rightTree.value)) {
      return false;
    }
    
    return equals(leftTree.left, rightTree.left) && equals(leftTree.right, rightTree.right);
  }
  
  private Node copy(Node source) {
    if (source == null) {
      return null;
    }
    Node node = new Node(source);
    node.left = copy(source.left);
    node.right = copy(source.right);
    return node;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    toString(sb, this.root);
    return sb.toString();
  }
  
  private static void toString(StringBuilder sb, Node node) {
    if (node == null) {
      return;
    }
    if (node.isOp() && node.left.isOp() && node.left.op.priority() < node.op.priority()) {
      sb.append("(");
    }
    toString(sb, node.left);
    if (node.isOp() && node.left.isOp() && node.left.op.priority() < node.op.priority()) {
      sb.append(")");
    }
    
    if (node.isArg()) {
      if (node.hasUnaryOp()) {
        sb.append(node.uOp.getSymbol());
      }
      sb.append(node.value.toString());
    }
    if (node.isOp()) {
      sb.append(" " + node.op.getSymbol() + " ");
    }
    
    if (node.right != null) {
      if (node.right.isOp()) {
        if (node.right.hasUnaryOp()) {
          sb.append(node.right.uOp.getSymbol());
        }
        if (node.right.op.priority() < node.op.priority()) {
          sb.append("(");
        }
      }
      toString(sb, node.right);
      if (node.right.isOp() && node.right.op.priority() < node.op.priority()) {
        sb.append(")");
      }
    }
  }
  
  static final class Node {
    Node left = null;
    Node right = null;
    
    Argument value = null;
    Operator op = null;
    UnaryOperator uOp = null;
    boolean validStruct = false;
    
    public Node() { }
    Node(Node other) {
      this.value = other.value;
      this.uOp = other.uOp;
      this.op = other.op;
      this.validStruct = other.validStruct;
    }
    
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
    
    static boolean evaluable(Argument arg, boolean precompute) {
      if (arg == null) {
        return false;
      }
      if (arg instanceof NotEvaluable) {
        if (precompute) {
          return false;
        }
        if (!((NotEvaluable)arg).isEvaluable()) {
          if (arg instanceof VariableArgument) {
            throw new NotEvaluableException("The variable '" + arg.toString() + "' is never "
                + "defined!");
          }
          throw new NotEvaluableException("Unable to evaluate the argument \"" + arg.toString()
              + "\" defined by the class \"" + arg.getClass().getName() + "\"."); 
        } 
      }
      return true;
    }
    
    boolean evaluate(boolean precompute) {
      boolean evaluatedChildren = false;
      if (this.isOp()) {
        if (!evaluable(this.left.value, precompute)) {
          return false;
        }
        if (!evaluable(this.right.value, precompute)) {
          return false;
        }
        this.value = this.op.evaluate(this.left.value, this.right.value);
        this.left = null;
        this.right = null;
        this.op = null;
        evaluatedChildren = true;
      }
      if (this.hasUnaryOp()) {
        if (!evaluable(this.value, precompute)) {
          return false;
        }
        this.value = this.uOp.evaluate(this.value);
        this.uOp = null;
      }
      evaluable(this.value, precompute);
      if (!precompute && this.value instanceof NotEvaluable) {
        this.value = ((NotEvaluable) value).convert();
      }
      return evaluatedChildren;
    }
  }
}