package com.github.losersclub.excalibor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.VariableArgument;
import com.github.losersclub.excalibor.argument.primitives.*;
import com.github.losersclub.excalibor.operator.Operator;
import com.github.losersclub.excalibor.operator.UnaryOperator;
import com.github.losersclub.excalibor.operator.primitives.*;

public class ExpressionCompiler {
  private final List<Argument> arguments;
  private final Map<String, Operator> operators;

  public ExpressionCompiler() {
    this(false);
  }

  public ExpressionCompiler(boolean noDefaults) {
    this.arguments = new ArrayList<Argument>();
    this.operators = new HashMap<String, Operator>();
    if (!noDefaults) {
      addPrimitives();
    }
  }

  public ExpressionCompiler(boolean noDefaults, List<Argument> args, List<Operator> ops) {
    this(noDefaults);
    for (Argument a : args) {
      this.addArgument(a);
    }
    for (Operator o : ops) {
      this.addOperator(o);
    }
  }

  private void addPrimitives() {
    this.arguments.addAll(Arrays.asList(
        new BooleanArgument(), new ByteArgument(), new CharArgument(), new DoubleArgument(),
        new FloatArgument(), new IntArgument(), new LongArgument(), new ShortArgument(),
        new StringArgument()));
    this.operators.put("+", new AddOperator());
    this.operators.put("&&", new AndOperator());
    this.operators.put("/", new DivideOperator());
    this.operators.put("==", new EqualsOperator());
    this.operators.put(">=", new GreaterThanEqOperator());
    this.operators.put(">", new GreaterThanOperator());
    this.operators.put("<=", new LessThanEqOperator());
    this.operators.put("<", new LessThanOperator());
    this.operators.put("%", new ModuloOperator());
    this.operators.put("*", new MultiplyOperator());
    this.operators.put("-", new NegateOperator());
    this.operators.put("!=", new NotEqualsOperator());
    this.operators.put("!", new NotOperator());
    this.operators.put("||", new OrOperator());
    this.operators.put("^", new XOrOperator());
  }

  public List<Argument> getArguments() {
    return this.arguments;
  }

  public Map<String, Operator> getOperators() {
    return this.operators;
  }

  Operator getOperator(String key) {
    return this.operators.get(key);
  }

  public void addArgument(Argument arg) {
    // Reject duplicate arguments
    for (Argument a : this.arguments) {
      if (a.getClass() == arg.getClass()) {
        throw new IllegalArgumentException("Duplicate Arguments found.");
      }
    }
    this.arguments.add(arg);
  }

  public boolean removeArgument(Argument arg) {
    return this.arguments.remove(arg);
  }

  public void addOperator(Operator op) {
    for (Operator o : this.operators.values()) {
      if (o.getClass() == op.getClass()) {
        throw new IllegalArgumentException("Duplicate Operators found.");
      }
      if (o.getSymbol().equals(op.getSymbol())) {
        throw new AmbiguousOperatorException("Duplicate operator symbols used: " + o.getSymbol()
            + ". Classes in conflict: " + o.getClass().getSimpleName() + ", "
            + op.getClass().getSimpleName());
      }
    }
    this.operators.put(op.getSymbol(), op);
  }

  public void removeOperator(Operator op) {
    this.operators.remove(op.getSymbol());
  }

  public void removeOperator(String opSymbol) {
    this.operators.remove(opSymbol);
  }

  EvalTree buildTree(String expression, Map<String, VariableArgument> variables)
      throws IllegalArgumentException, AmbiguousArgumentException {

    boolean mathContainerEncountered = false;
    if (expression == null || (expression = expression.trim()).length() == 0) {
      throw new IllegalArgumentException("Expression cannot be null or an empty string.");
    }

    EvalTree tree = new EvalTree();

    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      // Skip any extra white spaces
      if (Character.isWhitespace(c) && buffer.length() == 0) {
        continue;
      }
      // Handle containers
      if (c == '"' || c == '\'') {
        buffer.append(c);
        i = ExcaliborUtils.reachEndOfContainer(expression, i, c, buffer);
        buffer.append(c);
      } else if (c == '(') {
        mathContainerEncountered = buffer.length() == 0;
        if (!mathContainerEncountered) {
          buffer.append('(');
        }
        i = ExcaliborUtils.reachEndOfContainer(expression, i, ')', buffer);
        if (!mathContainerEncountered) {
          buffer.append(')');
        }
      } else if (c == '[') {
        mathContainerEncountered = buffer.length() == 0;
        if (!mathContainerEncountered) {
          buffer.append('[');
        }
        i = ExcaliborUtils.reachEndOfContainer(expression, i, ']', buffer);
        if (!mathContainerEncountered) {
          buffer.append(']');
        }
      } else if (ExcaliborUtils.isSymbol(c)) {
        if (buffer.length() > 0) {
          Argument arg = evaluateString(buffer.toString(), variables);
          tree.insert(arg);
          buffer.setLength(0);
        }
        while (ExcaliborUtils.isSymbol(expression.charAt(i))) {
          buffer.append(expression.charAt(i));
          i++;
        }
        i--;
        ParsedOperator op = parseOperator(buffer.toString(), 0);
        if (op == null) {
          throw new IllegalArgumentException("Unrecognized operator: " + buffer.toString());
        }
        tree.insert(op.op);
        if (op.isUnaryOpValid()) {
          tree.insert(op.unaryOp);
        }
        buffer.setLength(0);
        continue;

      } else if (Character.isWhitespace(c)) {
        // ignore it :) #efficient
      } else {
        buffer.append(c);
        if (i != expression.length() - 1) {
          continue;
        }
      }
      if (mathContainerEncountered) {
        EvalTree internalTree = buildTree(buffer.toString(), variables);
        tree.insert(internalTree);
        mathContainerEncountered = false;
      } else {
        Argument arg = evaluateString(buffer.toString(), variables);
        tree.insert(arg);
      }
      buffer.setLength(0);
    }
    return tree;

  }

  Argument evaluateString(String stringArg, Map<String, VariableArgument> variables)
      throws AmbiguousArgumentException, IllegalArgumentException {
    Argument viableArg = null;
    Argument parsedArg = null;
    for (Argument a : this.arguments) {
      if ((parsedArg = a.parse(stringArg)) != null) {
        if (viableArg != null) {
          throw new AmbiguousArgumentException(
              "More than one possible type for the argument: " + stringArg);
        }
        viableArg = parsedArg;
      }
    }
    if (viableArg == null) {
      if (ExcaliborUtils.isValidVarName(stringArg)) {
        if (!variables.containsKey(stringArg)) {
          variables.put(stringArg, new VariableArgument(this));
        }
        return variables.get(stringArg);
      }
      else {
        throw new IllegalArgumentException("Unrecognized argument: " + stringArg);
      }
    }
    return viableArg;
  }

  public Expression compile(String expression)
      throws IllegalArgumentException, AmbiguousArgumentException {
    Map<String, VariableArgument> variables = new HashMap<String, VariableArgument>();
    EvalTree tree = buildTree(expression, variables);
    tree.precompute();
    Expression expr = new Expression(tree, variables);
    return expr;
  }

  protected ParsedOperator parseOperator(final String expression, int startIndex) {
    List<ParsedOperator> potentialOperators = new ArrayList<ParsedOperator>(4);
    StringBuilder buffer = new StringBuilder();
    for (int i = startIndex; i < expression.length(); i++) {
      char c = expression.charAt(i);
      buffer.append(c);
      potentialOperators.add(new ParsedOperator(this.operators.get(buffer.toString())));
      for (int k = 0; k < buffer.length() - 1; k++) {
        if (potentialOperators.get(k).isOpValid()) {
          Operator unary = operators.get(buffer.toString().substring(k+1));
          if (unary instanceof UnaryOperator) {
            potentialOperators.get(k).isValid = true;
            potentialOperators.get(k).unaryOp = (UnaryOperator) unary;
          } else {
            potentialOperators.get(k).isValid = false;
            potentialOperators.get(k).unaryOp = null;
          }
        }
      }
    }

    for (int j = potentialOperators.size() - 1; j >= 0; j--) {
      ParsedOperator pop = potentialOperators.get(j);
      if (pop.isValid && pop.isOpValid()) {
        return potentialOperators.get(j);
      }
    }
    return null;
  }

  private static class ParsedOperator {
    Operator op;
    UnaryOperator unaryOp;
    boolean isValid = true;

    ParsedOperator(Operator op) {
      this(op, null);
    }

    ParsedOperator(Operator op, UnaryOperator uop) {
      this.op = op;
      this.unaryOp = uop;
    }

    boolean isOpValid() {
      return op != null;
    }

    boolean isUnaryOpValid() {
      return unaryOp != null;
    }
  }
}
