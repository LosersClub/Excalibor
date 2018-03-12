package losers.club.excalibor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.operator.Operator;

public class ExpressionCompiler {
  private final List<Argument> parsers;
  private final Map<String, Operator> operators; // Change to String tree for better performance?
  
  // Property to try and minimize tree/prerun during compile.
  private boolean canPrecompute = true;
  
  public ExpressionCompiler() {
    // TODO: default/fallback parsers & operators?
    this.parsers = new ArrayList<Argument>();
    this.operators = new HashMap<String, Operator>();
  }
  
  public void precompute(boolean value) {
    this.canPrecompute = value;
  }
  
  public void addArgument(Argument arg) {
    this.parsers.add(arg);
  }
  
  public boolean removeArugment(Argument arg) {
    return this.parsers.remove(arg);
  }
  
  public void addOperator(Operator op) {
    this.operators.put(op.getSymbol(), op);
  }
  
  public boolean removeOperator(Operator op) {
    return this.operators.remove(op.getSymbol(), op);
  }
  
  public Expression compile(String expression) {
    if (expression == null || (expression = expression.trim()).length() == 0) {
      throw new IllegalArgumentException("Expression cannot be null or an empty string.");
    }
    
    EvalTree tree = new EvalTree();
    
    // Attempt at iterative-based parsing
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
    }
    
    Expression expr = new Expression(tree);
    if (this.canPrecompute) {
      expr.precompute();
    }
    return expr;
  }
}
