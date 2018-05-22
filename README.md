# Excalibor
[![Build Status](https://img.shields.io/travis/LosersClub/Excalibor.svg?style=flat-square)](https://travis-ci.org/LosersClub/Excalibor) [![Codecov branch](https://img.shields.io/codecov/c/github/LosersClub/Excalibor.svg?style=flat-square)](https://codecov.io/gh/LosersClub/Excalibor) ![license](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square) ![Maven Central](https://img.shields.io/maven-central/v/losers.club/excalibor.svg?style=flat-square)
Excalibor is a dependency free extendable single-line expression parser that supports variables. Based on Java syntax, Excalibor is designed to closely emulate the same functionality as Java, meaning it will return the proper types and throw exceptions as you would expect if the expression was written in Java.

## Latest release

To use Excalibor, simply add the dependency to your project's build file (**Java 8+ required**).
For Maven in the `pom.xml`:
```xml
<dependency>
  <groupId>losers.club</groupId>
  <artifactId>excalibor</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle in `build.gradle`:
```gradle
dependencies {
  compile 'losers.club:excalibor:1.0.0'
}
```

## Usage
To quickly use Excalibor, you can use the `DefaultCompiler` and static functions in `Excalibor.java`:
```java
Excalibor.evaluate("4+3"); // returns 7
```
Alternatively you can create a new instance of the compiler and make the calls yourself:
```java
ExpressionCompiler compiler = new ExpressionCompiler();
Expression expr = compiler.compile("4 + 3");
expr.evaluate(); // returns 7
```
Using variables requires you set the variable to something before `evaluate` is called on the `Expression`. If not, a `NotEvaluableException` will be thrown. Below is a simple example of using variables:
```java
HashMap<String, Object> vars = new HashMap<String, Object>();
vars.put("x", 4);
vars.put("y", 6);
Excalibor.evaluate("(x + y)/2", vars); // returns 5
```
By design Excalibor has two core objects `ExpressionCompiler` and `Expression` where an `ExpressionCompiler` compiles a given string to an `Expression` where you can then set values to variables and evaluate the current `Expression` returning the resultant `Object`.

### ExpressionCompiler
A given `ExpressionCompiler` stores a set of `Arguments` and `Operators`. By default all primitive data types and operators are added to the compiler instance. To distable them, construct the `ExpressionCompiler` as:
```java
ExpressionCompiler noDefaults = new ExpressionCompiler(true);
```
In order for an operator to get regonized it must have an associated `Operator` class registered to its symbol. For example, the default `AddOperator` is registered to the symbol `+` making it a valid operator in a given expression. If you attempt to register an `Operator` to the compiler where its symbol is already in use a `AmbiguousOperatorException` will be thrown. If an `Operator` that you attempt to register is already registered, an `IllegalArgumentException` is thrown.

Every `Argument` class is a self-contained factory for making arguments. When an argument is read in an expression, it is parsed by all registered `Arguments`, if the given string is valid for an `Argument`, a new `Argument` instance is returned for that type. If a given argument string is valid for multiple `Argument` parsers, then an `AmbiguousArgumentException` is thrown. If it is valid for no parsers, a new `VariableArgument` will be created if it meets the conditions for a variable: cannot start with or contain only numbers, can contain but cannot be only underscores, and can't contain symbols (hold `_`).

To compile strings to `Expressions`, simply call `compile`:
```java
ExpressionCompiler compiler = new ExpressionCompiler();
compiler.compile("x + 4"); // returns a new Expression containing the variable "x"
```

### Expression
A given `Expression` has four possible ways to evaluate the stored expression. If `evaluate` is called without all variables set (or all `NotEvaluable` arguments with `isEvaluable() == true`) then a `NotEvaluableException` will be thrown.
```java
Object evaluate(); // Evaluates in current state
T evaluate(Class<? extends T>); // Evaluates and casts using the given Class
Object evaluate(Map<String, Object>); // Sets all variables in the Map and Evaluates
T evaluate(Map<String, Object>, Class<? extends T>); // Sets all variables in Map, Evaluates, and casts
```
To improve performance on evaluate, all `Expressions` will have a precomputed version of the expression. If no variable (or `Argument` that implements `NotEvaluable`) exists in the expression then the expression will be evaluated in the background and any call to `evaluate` will return the stored result. If a variable exists, then Excalibor will attempt to precompute any parts of the expression without variables. For example, the expression `3 + 4 + x` will be stored as `7 + x`.

When an `Expression` has a variable and evaluate is called, a copy of the interal expression is evaluated. This allows you to reuse existing expressions when you want to evaluate with a different value set to the variable. For example:
```java
ExpressionCompiler compiler = Excalibor.defaultCompiler();
Expression expr = compiler.compile("x + 4");
HashMap<String, Object> vars = new HashMap<String, Object>();
vars.put("x", 3);
expr.evaluate(vars); // returns 7
vars.put("x", 16);
expr.evaluate(vars); // returns 20
```
Note: For the primitive types, the type returned when `Expression` is evaluated will be the highest priority type used. From least to greatest that is `byte -> char -> short -> int -> long -> float -> double`. As an example, with the `DefaultCompiler` the expression `2 / 3` will return `0` with type `Integer` whereas `2 / 3.0` will return `0.6666666666666666` with type `Double`.

## License
This project is licensed under the terms of the MIT license
