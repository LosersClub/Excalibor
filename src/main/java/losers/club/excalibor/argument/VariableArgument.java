package losers.club.excalibor.argument;

import losers.club.excalibor.ExpressionCompiler;

public class VariableArgument implements Argument, NotEvaluable {
  private final ExpressionCompiler compiler;
  private Object obj = null;
  private boolean evaluable = false;
  
  public VariableArgument(ExpressionCompiler compiler) {
    this.compiler = compiler;
  }
  
  @Override
  public boolean isEvaluable() {
    return this.evaluable;
  }

  @Override
  public void setEvaluable(boolean value) {
    this.evaluable = value;
  }
  
  public void setValue(Object obj) {
    this.obj = obj;
    this.evaluable = this.obj != null;
  }

  @Override
  public Object getValue() {
    return obj;
  }
  
  @Override
  public Argument parse(String expression) {
    throw new UnsupportedOperationException("No parser exists for VariableArgument");
  }
  
  @Override
  public Argument build(Object obj) {
    throw new UnsupportedOperationException("No builder exists for VariableArgument");
  }
  
  @Override
  public Argument convert() {
    if (this.getValue() == null) {
      return null;
    }
    Argument out = null;
    for (Argument parser : compiler.getParsers()) {
      if ((out = parser.convert(this)) != null) {
        return out;
      }
    }
    return null;
  }
}
