# Excalibor
[![Build Status](https://img.shields.io/travis/LosersClub/Excalibor.svg?style=flat-square)](https://travis-ci.org/LosersClub/Excalibor) [![Codecov branch](https://img.shields.io/codecov/c/github/LosersClub/Excalibor.svg?style=flat-square)](https://codecov.io/gh/LosersClub/Excalibor) [![license](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square)](https://github.com/LosersClub/Excalibor/blob/master/LICENSE.md) [![Maven Central](https://img.shields.io/maven-central/v/com.github.losersclub/excalibor.svg?style=flat-square)](https://search.maven.org/artifact/com.github.losersclub/excalibor/)

Excalibor is a dependency free extendable single-line expression parser that supports variables. Based on Java syntax, Excalibor is designed to closely emulate the same functionality as Java, meaning it will return the proper types and throw exceptions as you would expect if the expression was written in Java.

## Latest release

To use Excalibor, simply add the dependency to your project's build file (**Java 8+ required**).

For Maven in the `pom.xml`:
```xml
<dependency>
  <groupId>com.github.losersclub</groupId>
  <artifactId>excalibor</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle in `build.gradle`:
```gradle
dependencies {
  compile 'com.github.losersclub:excalibor:1.0.0'
}
```

## Quick Usage

By design, Excalibor has two core objects, `ExpressionCompiler` and `Expression`. An `ExpressionCompiler` compiles a given `String` to an `Expression`, which may or may not contain variables. If variables are present, you must assign values to the variables. Then, you can evaluate the current `Expression` to obtain the resulting `Object`.

For any usage of Excalibor, the expression must take the form of a `String`. This means that if you'd like to do operations on `Strings`, you need to escape quotes inside the `String` expression. As an example: 
```java
Excalibor.evaluate("\"Hello\" + \" World!\""); // returns "Hello World!"
```

To quickly use Excalibor, you can use the `DefaultCompiler` and the related static functions in `Excalibor.java`:
```java
Excalibor.evaluate("4+3"); // returns 7
```
Alternatively you can create a new instance of the `ExpressionCompiler` (and customize it) and make the calls yourself:
```java
ExpressionCompiler compiler = new ExpressionCompiler();
Expression expr = compiler.compile("4 + 3");
expr.evaluate(); // returns 7
```
Using variables requires you set the variable to something before `evaluate` is called on the `Expression`. If not, a `NotEvaluableException` will be thrown. Below is a simple example of variable usage:
```java
HashMap<String, Object> vars = new HashMap<String, Object>();
vars.put("x", 4);
vars.put("y", 6);
Excalibor.evaluate("(x + y)/2", vars); // returns 5
```
### ExpressionCompiler
A given `ExpressionCompiler` stores a set of `Arguments` and `Operators`. By default, all primitive data types and operators (bitwise and assignment excluded) are added to the `ExpressionCompiler` instance. To disable them, construct the `ExpressionCompiler` as:
```java
ExpressionCompiler noDefaults = new ExpressionCompiler(true);
```

You can customize which `Arguments` and `Operators` are present in the `ExpressionCompiler` as well. There is an `ExpressionCompiler` constructor that can take in a `List` of `Arguments` and a `List` of `Operators` to be registered. The first `boolean` argument, if set to `true`, will not include the default `Arguments` and `Operators`.

```java
List<Argument> args = Arrays.asList(new IntArgument(), new DoubleArgument());
List<Operator> ops = Arrays.asList(new AddOperator());
ExpressionCompiler compiler = new ExpressionCompiler(true, args, ops); // Contains only the two Arguments and one Operator
```

Alternatively, you can manually add `Arguments` and `Operators` to an existing `ExpressionCompiler`:
```java
ExpressionCompiler noDefaults = new ExpressionCompiler(true);
noDefaults.addArgument(new IntArgument());
noDefaults.addOperator(new AddOperator());
```

In order for an operator to be regonized in an expression, it must have an associated `Operator` class registered to its symbol. For example, the default `AddOperator` is registered to the symbol `+`, making it a valid operator in a given expression. If you attempt to register an `Operator` to an `ExpressionCompiler` where its symbol is already in use, an `AmbiguousOperatorException` will be thrown. If an `Operator` that you attempt to register is already registered, an `IllegalArgumentException` is thrown.

Every `Argument` class is a self-contained factory for making arguments (the non-operator components of an expression). When an argument is read in an expression, it is parsed by all registered `Arguments`. If the given `String` is valid for an `Argument`, a new `Argument` instance is returned for that type. For instance, the `FloatArgument` will recognize `"4.5f"`. If the `String` is valid for multiple `Argument` parsers, then an `AmbiguousArgumentException` is thrown. If it is valid for no parsers and the `String` follows the rules for a valid variable name, a new `VariableArgument` will be created (and the variable must be defined at some point). To be a valid variable name, the `String` cannot start with or contain only numbers, can contain but cannot contain only underscores, and can't contain symbols (hold `_`). If the `String` cannnot be parsed by any `Argument` and is not a valid variable name, an `IllegalArgumentException` will be thrown.

To compile `Strings` to `Expressions`, simply call `compile` on the `ExpressionCompiler`:
```java
ExpressionCompiler compiler = new ExpressionCompiler();
Expression expr = compiler.compile("x + 4"); // returns a new Expression containing the variable "x"
```
### Custom Arguments and Operators
You can write your own `Arguments` and `Operators` as well.

To create a new `Argument`, write a class that extends the abstract `Argument` class or one of its children. There are more specific classifications of the `Argument` that we recommend extending in combinations instead of `Argument` directly, as they add useful functionality: `ComparableArgument`, `LogicalArgument`, `NumberArgument`, and `EqualsArgument`. The first adds comparison methods (like greater than, less than, etc.). The second is for something like `BooleanArgument`, which uses `boolean` values and `||` (or) and similar operations. The third is for `Numbers` and enables math methods. The last just adds `equals` and `notEquals` methods, and is implicitly extended by the first three. Note that `NumberArguments` require a priority value, which is used in the background for casting (doubles are the highest priority). Once you have extended the relevant abstract classes and overriden all methods (including the parser!), register the `Argument` with your `ExpressionCompiler` and relevant arguments should be recognized in input expressions. 

Creating a new `Operator` is much the same as creating a new `Argument`. Simply extend the abstract `Operator` class and override all methods. Make sure to assign a unique symbol and priority for your `Operator`, and then register it to your `ExpressionCompiler`. 

### Expression
A given `Expression` has four possible ways to evaluate the stored expression. If `evaluate` is called without all variables set (or all arguments are `NotEvaluable` while `isEvaluable() == true`) then a `NotEvaluableException` will be thrown.

These are the important `Expression` methods:
```java
Object evaluate(); // Evaluates in current state
T evaluate(Class<? extends T>); // Evaluates and casts using the given Class
Object evaluate(Map<String, Object>); // Sets all variables in the Map and Evaluates
T evaluate(Map<String, Object>, Class<? extends T>); // Sets all variables in Map, Evaluates, and Casts
```
To improve performance on evaluate, all `Expressions` will have a precomputed version of the expression. If no variable (or `Argument` that implements `NotEvaluable`) exists in the expression then the expression will be immediately evaluated in the background and any call to `evaluate` will return the stored result. If a variable exists, then Excalibor will attempt to precompute any parts of the expression without variables. For example, the expression `3 + 4 + x` will be stored as `7 + x`.

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
