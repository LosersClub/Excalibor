package com.github.losersclub.excalibor;

import static com.github.losersclub.excalibor.EvalTreeTest.isSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.losersclub.excalibor.AmbiguousArgumentException;
import com.github.losersclub.excalibor.AmbiguousOperatorException;
import com.github.losersclub.excalibor.EvalTree;
import com.github.losersclub.excalibor.Expression;
import com.github.losersclub.excalibor.ExpressionCompiler;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.VariableArgument;
import com.github.losersclub.excalibor.argument.primitives.BooleanArgument;
import com.github.losersclub.excalibor.argument.primitives.ByteArgument;
import com.github.losersclub.excalibor.argument.primitives.CharArgument;
import com.github.losersclub.excalibor.argument.primitives.DoubleArgument;
import com.github.losersclub.excalibor.argument.primitives.FloatArgument;
import com.github.losersclub.excalibor.argument.primitives.IntArgument;
import com.github.losersclub.excalibor.argument.primitives.LongArgument;
import com.github.losersclub.excalibor.argument.primitives.ShortArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;
import com.github.losersclub.excalibor.operator.Operator;
import com.github.losersclub.excalibor.operator.primitives.AddOperator;
import com.github.losersclub.excalibor.operator.primitives.NegateOperator;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionCompilerTest {

  private ExpressionCompiler expComp;
  private Map<String, VariableArgument> variables;

  @Before
  public void before() {
    this.expComp = new ExpressionCompiler();
    this.variables = new HashMap<String, VariableArgument>();
  }

  @Test
  public void testNonDefaultConstructors() {
    ExpressionCompiler noDefaults = new ExpressionCompiler(true);
    Assert.assertTrue(noDefaults.getArguments().size() == 0);
    ExpressionCompiler defaults = new ExpressionCompiler(false);
    Assert.assertTrue(defaults.getArguments().size() == 9);
    Assert.assertTrue(defaults.getOperators().size() == 15);
    List<Argument> args = Arrays.asList(new IntArgument(), new DoubleArgument());
    List<Operator> ops = Arrays.asList(new AddOperator());
    ExpressionCompiler noDefaultsArgsOps = new ExpressionCompiler(true, args, ops);
    Assert.assertTrue(noDefaultsArgsOps.getArguments().size() == 2);
    Assert.assertTrue(noDefaultsArgsOps.getOperators().size() == 1);
  }

  @Test
  public void testAddRemoveArgumentLegal() {
    ExpressionCompiler noDefaults = new ExpressionCompiler(true);
    IntArgument intArg = new IntArgument();
    Assert.assertTrue(noDefaults.getArguments().size() == 0);
    noDefaults.addArgument(intArg);
    Assert.assertTrue(noDefaults.getArguments().size() == 1
        && noDefaults.getArguments().contains(intArg));
    noDefaults.addArgument(new FloatArgument());
    noDefaults.removeArgument(intArg);
    Assert.assertTrue(noDefaults.getArguments().size() == 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddArgumentIllegal() {
    List<Argument> args = Arrays.asList(new IntArgument(), new FloatArgument());
    List<Operator> ops = Arrays.asList(new AddOperator());
    ExpressionCompiler noDefaultsArgsOps = new ExpressionCompiler(true, args, ops);
    noDefaultsArgsOps.addArgument(new FloatArgument());
  }

  @Test
  public void testAddRemoveOperatorLegal() {
    ExpressionCompiler noDefaults = new ExpressionCompiler(true);
    AddOperator addOp = new AddOperator();
    Assert.assertTrue(noDefaults.getOperators().size() == 0);
    noDefaults.addOperator(addOp);
    Assert.assertTrue(noDefaults.getOperators().size() == 1
        && noDefaults.getOperators().containsValue(addOp));
    noDefaults.addOperator(new NegateOperator());
    noDefaults.removeOperator(addOp);
    Assert.assertTrue(noDefaults.getOperators().size() == 1);
    noDefaults.removeOperator("-");
    Assert.assertTrue(noDefaults.getOperators().size() == 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddOperatorIllegal() {
    List<Argument> args = Arrays.asList(new IntArgument(), new FloatArgument());
    List<Operator> ops = Arrays.asList(new AddOperator());
    ExpressionCompiler noDefaultsArgsOps = new ExpressionCompiler(true, args, ops);
    noDefaultsArgsOps.addOperator(new AddOperator());
  }

  @Test(expected = AmbiguousOperatorException.class)
  public void testAddOperatorDuplicateIllegal() {
    AddOperator opOne = mock(AddOperator.class);
    NegateOperator opTwo = mock(NegateOperator.class);
    ExpressionCompiler noDefaults = new ExpressionCompiler(true);
    when(opOne.getSymbol()).thenReturn("~");
    when(opTwo.getSymbol()).thenReturn("~");
    noDefaults.addOperator(opOne);
    noDefaults.addOperator(opTwo);
  }

  @Test
  public void testEvaluateString() throws Exception {
    Assert.assertTrue(this.expComp.evaluateString("true", this.variables)
        instanceof BooleanArgument);
    Assert.assertTrue(this.expComp.evaluateString("100b", this.variables)
        instanceof ByteArgument);
    Assert.assertTrue(this.expComp.evaluateString("\'a\'", this.variables)
        instanceof CharArgument);
    Assert.assertTrue(this.expComp.evaluateString("100.2", this.variables)
        instanceof DoubleArgument);
    Assert.assertTrue(this.expComp.evaluateString("100.2f", this.variables)
        instanceof FloatArgument);
    Assert.assertTrue(this.expComp.evaluateString("100", this.variables)
        instanceof IntArgument);
    Assert.assertTrue(this.expComp.evaluateString("100L", this.variables)
        instanceof LongArgument);
    Assert.assertTrue(this.expComp.evaluateString("100s", this.variables)
        instanceof ShortArgument);
    Assert.assertTrue(this.expComp.evaluateString("\"Hello World!\"", this.variables)
        instanceof StringArgument);
  }

  @Test(expected = AmbiguousArgumentException.class)
  public void testEvaluateStringMultipleArgs() throws Exception {
    IntArgument intArg = mock(IntArgument.class);
    DoubleArgument doubleArg = mock(DoubleArgument.class);
    ExpressionCompiler noDefaults = new ExpressionCompiler(true);
    noDefaults.addArgument(intArg);
    noDefaults.addArgument(doubleArg);
    when(intArg.parse("yay!")).thenReturn(new IntArgument());
    when(doubleArg.parse("yay!")).thenReturn(new DoubleArgument());
    noDefaults.evaluateString("yay!", this.variables);
  }

  @Test(expected = InvalidExpressionException.class)
  public void testEvaluateStringInvalidVarName() throws Exception {
    this.expComp.evaluateString(".abc", this.variables);
  }

  @Test(expected=InvalidExpressionException.class)
  public void testBuildTreeInvalidInput()
      throws InvalidExpressionException, AmbiguousArgumentException {
    this.expComp.buildTree(null, this.variables);
  }

  @Test(expected=InvalidExpressionException.class)
  public void testBuildTreeEmpty() throws Exception {
    this.expComp.buildTree("\t\n  ", this.variables);
  }

  @Test
  public void testBuildTreeLogical() {
    // true || true ^ false
    EvalTree expected = new EvalTree();
    expected.insert(new BooleanArgument(true));
    expected.insert(expComp.getOperator("||"));
    expected.insert(new BooleanArgument(true));
    expected.insert(expComp.getOperator("^"));
    expected.insert(new BooleanArgument(false));

    EvalTree actual = this.expComp.buildTree("true||true^false", this.variables);
    assertThat(actual, isSame(expected.getRoot()));
    Assert.assertTrue((boolean)actual.evaluate().getValue());
  }

  @Test
  public void testBuildTreeContainers() {
    // ([-2*8 + 4.5f] % 5s) + 'c' + " World" + '!'
    EvalTree expected = new EvalTree();
    EvalTree parenTree = new EvalTree();
    EvalTree bracketTree = new EvalTree();
    bracketTree.insert(expComp.getOperator("-"));
    bracketTree.insert(new IntArgument(2));
    bracketTree.insert(expComp.getOperator("*"));
    bracketTree.insert(new IntArgument(8));
    bracketTree.insert(expComp.getOperator("+"));
    bracketTree.insert(new FloatArgument(4.5f));
    parenTree.insert(bracketTree);
    parenTree.insert(expComp.getOperator("%"));
    parenTree.insert(new ShortArgument((short)5));
    expected.insert(parenTree);
    expected.insert(expComp.getOperator("+"));
    expected.insert(new CharArgument('c'));
    expected.insert(expComp.getOperator("+"));
    expected.insert(new StringArgument(" World"));
    expected.insert(expComp.getOperator("+"));
    expected.insert(new CharArgument('!'));

    String expression = "([-2*8 + 4.5f] % 5s) + 'c' + \" World\" + '!'";
    EvalTree actual = expComp.buildTree(expression, variables);
    assertThat(actual, isSame(expected.getRoot()));
  }

  @Test(expected = InvalidExpressionException.class)
  public void testBuildTreeUnclosedContainer() {
    String expression = "(4 + 3";
    expComp.buildTree(expression, variables);
  }

  @Test
  public void testBuildTreeNestedContainers() {
    String expression = "((\"Hello \\\"Some quote\\\"\" + \" World\") + \'\\\'\')";
    EvalTree expected = new EvalTree();
    EvalTree parenTree = new EvalTree();
    parenTree.insert(new StringArgument("Hello \"Some quote\""));
    parenTree.insert(expComp.getOperator("+"));
    parenTree.insert(new StringArgument(" World"));
    expected.insert(parenTree);
    expected.insert(expComp.getOperator("+"));
    expected.insert(new CharArgument('\''));
    EvalTree actual = expComp.buildTree(expression, variables);
    assertThat(actual, isSame(expected.getRoot()));

    expression = "\"(abc)\"";
    expected = new EvalTree();
    expected.insert(new StringArgument("(abc)"));
    actual = expComp.buildTree(expression, variables);
    assertThat(actual, isSame(expected.getRoot()));
  }

  @Test(expected = InvalidExpressionException.class)
  public void testBuildTreeVarFxn() {
    // Test something that would be a function call. By default, it'll throw, but
    // if someone adds a function Argument or something it should work.
    // The string would be something like: "abc(def)"
    // Or, it could be an index access like: var[x]
    String expression = "abc(def)";
    expComp.buildTree(expression, variables);
  }

  @Test(expected = InvalidExpressionException.class)
  public void testBuildTreeVarIndexAccess() {
    //Test something that would be an index access. By default, it'll throw, but
    // if someone adds a function Argument or something it should work.
    // The string would be something like: "var[x]"
    String expression = "abc[x]";
    expComp.buildTree(expression, variables);
  }

  @Test
  public void testBuildTreeConfusingOperators() {
    // -4--4
    EvalTree expected = new EvalTree();
    expected.insert(expComp.getOperator("-"));
    expected.insert(new IntArgument(4));
    expected.insert(expComp.getOperator("-"));
    expected.insert(expComp.getOperator("-"));
    expected.insert(new IntArgument(4));
    String expression = "-4--4";
    EvalTree actual = expComp.buildTree(expression, variables);
    assertThat(actual, isSame(expected.getRoot()));
  }

  @Test(expected = InvalidExpressionException.class)
  public void testBuildTreeBadOperator() {
    // 4 -/5
    String expression = "4 -/5";
    expComp.buildTree(expression, variables);
  }

  @Test(expected = InvalidExpressionException.class)
  public void testBuildTreeNonexistantOperator() {
    // 4 ~5
    String expression = "4 ~5";
    expComp.buildTree(expression, variables);
  }

  @Test
  public void testBuildTreeVariables() {
    String expression = "([(x + 5) - 11] / y) + x";
    expComp.buildTree(expression, variables);
    Assert.assertTrue(this.variables.containsKey("x"));
    Assert.assertTrue(this.variables.containsKey("y"));
  }

  @Test
  public void testCompile() {
    String expression = "([(x + 5) - 11] / y) + x";
    Expression expr = expComp.compile(expression);
    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("x", 14);
    vars.put("y", 2);
    Assert.assertEquals(18, expr.evaluate(vars));
  }

  @Test
  public void testBuildTreeOneUnaryOp() {
    //-5
    String expression = "-5";
    expComp.buildTree(expression, variables);
  }
}
