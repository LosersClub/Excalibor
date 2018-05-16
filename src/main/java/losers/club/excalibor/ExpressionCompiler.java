package losers.club.excalibor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.BooleanArgument;
import losers.club.excalibor.argument.primitives.ByteArgument;
import losers.club.excalibor.argument.primitives.CharArgument;
import losers.club.excalibor.argument.primitives.DoubleArgument;
import losers.club.excalibor.argument.primitives.FloatArgument;
import losers.club.excalibor.argument.primitives.IntArgument;
import losers.club.excalibor.argument.primitives.LongArgument;
import losers.club.excalibor.argument.primitives.ShortArgument;
import losers.club.excalibor.argument.primitives.StringArgument;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;

public class ExpressionCompiler {
  public final List<Argument> parsers;
  private final Map<String, Operator> operators; // Change to String tree for better performance?
  
  // Property to try and minimize tree/prerun during compile.
  private boolean canPrecompute = true;
  
  public ExpressionCompiler() {
    // TODO: default/fallback parsers & operators?
    this.parsers = new ArrayList<Argument>();
    this.operators = new HashMap<String, Operator>();
    
    this.parsers.addAll(Arrays.asList(
        new BooleanArgument(), new ByteArgument(), new CharArgument(),
        new DoubleArgument(), new FloatArgument(), new IntArgument(),
        new LongArgument(), new ShortArgument(), new StringArgument()));
  }
  
  public Collection<Argument> getParsers() {
    return Collections.unmodifiableList(this.parsers);
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
  
  private Argument getArgument(String expression) {
    Argument out = null;
    for (Argument arg : parsers) {
      Argument temp = null;
      if ((temp = arg.parse(expression)) != null) {
        if (out != null) {
          throw new RuntimeException("Ambiguous parse expression between " + out + " and " + temp);
        }
        out = temp;
      }
    }
    return out;
  }
  
  private boolean handleInQuotes(StringBuilder buffer, char c) {
    if (!inQuotes) {
      return false;
    }
    buffer.append(c);
    if (c == buffer.charAt(0) && buffer.charAt(buffer.length() - 1) != '\\') {
      inQuotes = false;
      needOperator = true;
      // TODO: parse for argument and add to tree
      buffer.setLength(0);
    }
    return true;
  }
  
  private boolean inQuotes = false;
  private boolean needOperator = false;
  int stackDepth = 0;
  
  public Expression compile(String expression) {
    if (expression == null || (expression = expression.trim()).length() == 0) {
      throw new IllegalArgumentException("Expression cannot be null or an empty string.");
    }
    
    EvalTree tree = new EvalTree();
    
    // Attempt at iterative-based parsing
    
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      
      if (Character.isWhitespace(c) && buffer.length() == 0) {
        continue;
      }
      
      if (handleInQuotes(buffer, c)) {
        continue;
      }
      
      // Starting checks.
      if (buffer.length() == 0) {
        if (!needOperator) {
          if (c == '\'' || c == '"') {
            inQuotes = true;
            buffer.append(c);
            continue;
          }
          // TODO: Handle eval depth call
          if (c == '(') {
            stackDepth++;
            // TODO: inform tree
            continue;
          }
          
          if (isSymbol(c)) {
            int j = i;
            while (isSymbol(expression.charAt(j))) {
              buffer.append(expression.charAt(j));
              j++;
            }
            Operator op = operators.get(buffer.toString());
            if (op instanceof UnaryOperator) {
              // TODO: Add operator to tree?
              i = j - 1;
              buffer.setLength(0);
              continue;
            }
            throw new RuntimeException();
          }
        } else {
          if (c == ')') {
            stackDepth--;
            // TODO: inform tree.
            continue;
          }
          int j = i;
          while (isSymbol(expression.charAt(j))) {
            buffer.append(expression.charAt(j));
            j++;
          }
          ParsedOperator op = parseOperator(buffer.toString(), 0);
          // TODO: Add op and optional unary op to tree.
        } 
      } 
    }
    
    if (stackDepth != 0 || inQuotes == true || needOperator == false) {
      throw new RuntimeException("Invalid Expression");
    }
    
//    Expression expr = new Expression(tree, null);
    if (this.canPrecompute) {
//      expr.precompute();
    }
    return null;
  }
  
  private static boolean isSymbol(char c) {
    return !Character.isWhitespace(c) && !Character.isDigit(c) && !Character.isLetter(c);
  }
  
  protected ParsedOperator parseOperator(final String expression, int startIndex) {
    List<ParsedOperator> potentialOperators = new ArrayList<ParsedOperator>(4);
    StringBuilder buffer = new StringBuilder();
    for (int i = startIndex; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (Character.isWhitespace(c) || Character.isDigit(c) || Character.isLetter(c)) {
        for (int j = potentialOperators.size() - 1; j != 0; j--) {
          ParsedOperator pop = null;
          if ((pop = potentialOperators.get(j)) != null) {
            if (pop.endIndex == i - 1 || pop.unaryOp != null) {
              return potentialOperators.get(j);
            }
          }
        }
        return null;
      }
      buffer.append(c);
      potentialOperators.add(new ParsedOperator(operators.get(buffer.toString()), i));
      for (int k = 0; k < buffer.length() - 1; k++) {
        if (potentialOperators.get(k).isValid()) {
          Operator unary = operators.get(buffer.toString().substring(k));
          if (unary != null && unary instanceof UnaryOperator) {
            potentialOperators.get(k).unaryOp = (UnaryOperator) unary;
          }
        }
      }
    }
    return null;
  }
  
  private static class ParsedOperator {
    Operator op;
    UnaryOperator unaryOp;
    int endIndex;
    
    ParsedOperator(Operator op, int endIndex) {
      this(op, null, endIndex);
    }
    
    ParsedOperator(Operator op, UnaryOperator uop, int endIndex) {
      this.op = op;
      this.unaryOp = uop;
      this.endIndex = endIndex;
    }
    
    boolean isValid() {
      return op != null;
    }
  }
}
