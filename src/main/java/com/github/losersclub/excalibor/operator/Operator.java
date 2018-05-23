package com.github.losersclub.excalibor.operator;

import com.github.losersclub.excalibor.argument.Argument;

public abstract class Operator {
  
  public abstract String getSymbol();
  
  public abstract int priority();
  
  public abstract Argument evaluate(Argument lhs, Argument rhs);
  
  @Override
  public int hashCode() {
    return getSymbol().hashCode();
  }
}
