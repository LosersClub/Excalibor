package com.github.losersclub.excalibor;

import java.util.Map;

public final class Excalibor {
  private Excalibor() { }
  
  private static ExpressionCompiler defaultCompiler = new ExpressionCompiler();
  
  public static ExpressionCompiler defaultCompiler() {
    return defaultCompiler;
  }
  
  public static ExpressionCompiler setDefaultCompiler(ExpressionCompiler compiler) {
    if (compiler != null) {
      defaultCompiler = compiler;
    }
    return defaultCompiler;
  }
  
  public static Object evaluate(String expression, ExpressionCompiler compiler) {
    return compiler.compile(expression).evaluate();
  }
  
  public static Object evaluate(String expression) {
    return defaultCompiler.compile(expression).evaluate();
  }
  
  public static <T> T evaluate(String expression, Class<? extends T> typeOf) {
    return defaultCompiler.compile(expression).evaluate(typeOf);
  }
  
  public static Object evaluate(String expression, Map<String, Object> vars) {
    return defaultCompiler.compile(expression).evaluate(vars);
  }
  
  public static <T> T evaluate(String expression, Map<String, Object> vars,
      Class<? extends T> typeOf) {
    return defaultCompiler.compile(expression).evaluate(vars, typeOf);
  }
  
  public static Expression compile(String expression, ExpressionCompiler compiler) {
    return compiler.compile(expression);
  }
  
  public static Expression compile(String expression) {
    return defaultCompiler.compile(expression);
  }
}
