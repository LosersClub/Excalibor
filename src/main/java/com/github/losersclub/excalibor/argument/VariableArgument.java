package com.github.losersclub.excalibor.argument;

import com.github.losersclub.excalibor.ExpressionCompiler;

public class VariableArgument extends Argument implements NotEvaluable {
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
    for (Argument parser : compiler.getArguments()) {
      if ((out = parser.convert(this)) != null) {
        return out;
      }
    }
    return null;
  }
}
