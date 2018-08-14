package com.github.losersclub.excalibor.argument;

import com.github.losersclub.excalibor.ExpressionCompiler;

public class VariableArgument extends Argument implements NotEvaluable {
  private final ExpressionCompiler compiler;
  private final String key;
  private Object obj = null;
  private boolean evaluable = false;

  public VariableArgument(ExpressionCompiler compiler, String key) {
    this.compiler = compiler;
    this.key = key;
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
  
  public String getKey() {
    return this.key;
  }
  
  @Override
  public String toString() {
    return this.getKey();
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
      return this;
    }
    Argument out = null;
    for (Argument parser : compiler.getArguments()) {
      if ((out = parser.convert(this)) != null) {
        return out;
      }
    }
    return this;
  }
}
