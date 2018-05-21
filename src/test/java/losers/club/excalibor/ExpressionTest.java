package losers.club.excalibor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.NotEvaluable;
import losers.club.excalibor.argument.VariableArgument;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;


@RunWith(MockitoJUnitRunner.class)
public class ExpressionTest {
  @Mock Argument arg;
  @Mock NotEvaluableArg nArg;
  @Mock Operator op;
  EvalTree tree = new EvalTree();
  HashMap<String, VariableArgument> variables = new HashMap<String, VariableArgument>(); 
  
  @Before
  public void before() {
    when(arg.getValue()).thenReturn("arg");
    when(op.getSymbol()).thenReturn("+");
  }
  
  @Test
  public void evaluateNoCopy() {
    tree.insert(arg);
    Expression expr = new Expression(tree);
    assertThat(expr.evaluate(), is("arg"));
  }
  
  @Test
  public void evaluateCast() {
    tree.insert(arg);
    Expression expr = new Expression(tree);
    assertThat(expr.evaluate(String.class), is("arg"));
  }
  
  @Test(expected = ClassCastException.class)
  public void evaluateBadCast() {
    tree.insert(arg);
    Expression expr = new Expression(tree);
    expr.evaluate(Integer.class);
  }
  
  @Test
  public void evaluateWithOp() {
    when(op.evaluate(arg, arg)).thenReturn(arg);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.getRoot().op, is(op));
    Expression expr = new Expression(tree);
    assertThat(expr.evaluate(), is("arg"));
    assertThat(tree.getRoot().op, is(op));
    verify(op, times(1)).evaluate(arg, arg);
  }
  
  @Test
  public void evaluateNotEvaluable() {
    when(nArg.isEvaluable()).thenReturn(true);
    when(nArg.convert()).thenReturn(arg);
    tree.insert(nArg);
    assertThat(tree.getRoot().value, is(nArg));
    Expression expr = new Expression(tree);
    assertThat(expr.evaluate(), is("arg"));
    assertThat(tree.getRoot().value, is(nArg));
    verify(nArg, times(1)).convert();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void setVariableDoesntExist() {
    HashMap<String, Object> vars = new HashMap<String, Object>();
    vars.put("x", null);
    Expression expr = new Expression(tree);
    expr.evaluate(vars);
  }
  
  @Test
  public void setVariableEvaluate() {
    VariableArgument vArg = mock(VariableArgument.class);
    Object obj = new Object();
    HashMap<String, Object> vars = new HashMap<String, Object>();
    vars.put("x", obj);
    variables.put("x", vArg);
    tree.insert(arg);
    Expression expr = new Expression(tree, variables);
    expr.evaluate(vars);
    verify(vArg, times(1)).setValue(obj);
  }
  
  @Test
  public void setVariableEvaluateCast() {
    VariableArgument vArg = mock(VariableArgument.class);
    Object obj = new Object();
    HashMap<String, Object> vars = new HashMap<String, Object>();
    vars.put("x", obj);
    variables.put("x", vArg);
    tree.insert(arg);
    Expression expr = new Expression(tree, variables);
    expr.evaluate(vars, String.class);
    verify(vArg, times(1)).setValue(obj);
  }
  
  @Test
  public void evaluateVariableSame() {
    VariableArgument vArg = mock(VariableArgument.class);
    when(vArg.isEvaluable()).thenReturn(true);
    when(vArg.convert()).thenReturn(arg);
    when(op.evaluate(arg, arg)).thenReturn(arg);
    variables.put("x", vArg);
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(vArg);
    
    Expression expr = new Expression(tree, variables);
    Object obj = new Object();
    expr.setVariable("x", obj);
    assertThat(expr.evaluate(), is("arg"));
    verify(vArg, times(1)).setValue(obj);
    verify(vArg, times(2)).convert();
  }
  
  @Test
  public void toStringTest() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    Expression expr = new Expression(tree);
    assertThat(expr.toString(), is("arg + arg"));
  }
  
  @Test
  public void toStringUOp() {
    UnaryOperator uOp = mock(UnaryOperator.class);
    when(uOp.getSymbol()).thenReturn("-");
    tree.insert(arg);
    tree.insert(op);
    tree.insert(uOp);
    tree.insert(arg);
    Expression expr = new Expression(tree);
    assertThat(expr.toString(), is("arg + -arg"));
  }
  
  @Test
  public void toStringDepth() {
    tree.insert(arg);
    tree.insert(op);
    EvalTree tree2 = new EvalTree();
    tree2.insert(arg);
    tree2.insert(op);
    tree2.insert(arg);
    tree.insert(tree2);
    Expression expr = new Expression(tree);
    assertThat(expr.toString(), is("arg + (arg + arg)"));
  }
  
  @Test
  public void toStringDepthUOp() {
    UnaryOperator uOp = mock(UnaryOperator.class);
    when(uOp.getSymbol()).thenReturn("-");
    tree.insert(arg);
    tree.insert(op);
    EvalTree tree2 = new EvalTree();
    tree2.insert(arg);
    tree2.insert(op);
    tree2.insert(arg);
    tree.insert(uOp);
    tree.insert(tree2);
    Expression expr = new Expression(tree);
    assertThat(expr.toString(), is("arg + -(arg + arg)"));
  }
  
  @Test
  public void toStringVariableNotInMap() {
    VariableArgument vArg = mock(VariableArgument.class);
    variables.put("y", mock(VariableArgument.class));
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(arg);
    Expression expr = new Expression(tree, variables);
    assertThat(expr.toString(), is("null + arg"));
  }
  
  @Test
  public void toStringVariable() {
    VariableArgument vArg = mock(VariableArgument.class);
    variables.put("x", vArg);
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(arg);
    Expression expr = new Expression(tree, variables);
    assertThat(expr.toString(), is("x + arg"));
  }
  
  @Test
  public void toStringVariableSame() {
    VariableArgument vArg = mock(VariableArgument.class);
    variables.put("x", vArg);
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(vArg);
    Expression expr = new Expression(tree, variables);
    assertThat(expr.toString(), is("x + x"));
  }
  
  private static abstract class NotEvaluableArg implements Argument, NotEvaluable { }
}
