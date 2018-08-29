package com.github.losersclub.excalibor;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.losersclub.excalibor.EvalTree;
import com.github.losersclub.excalibor.NotEvaluableException;
import com.github.losersclub.excalibor.EvalTree.Node;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.NotEvaluable;
import com.github.losersclub.excalibor.argument.VariableArgument;
import com.github.losersclub.excalibor.operator.Operator;
import com.github.losersclub.excalibor.operator.UnaryOperator;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EvalTreeTest {
  @Mock private Argument arg;
  @Mock private Operator op;
  @Mock private UnaryOperator uOp;
  EvalTree tree = new EvalTree();
  
  @Before
  public void before() {
    when(op.getSymbol()).thenReturn("+");
    when(arg.toString()).thenReturn("\"arg\"");
    when(uOp.getSymbol()).thenReturn("-");
  }
  
  @Test
  public void singleArgTree() {
    tree.insert(arg);
    Assert.assertTrue(tree.valid());
    assertThat(tree.size(), is(1));
  }
  
  @Test
  public void insertUnary() {
    tree.insert(uOp);
    tree.insert(arg);
    assertThat(tree.size(), is(1));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void insertExpression() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void insertNoOp() {
    tree.insert(arg);
    tree.insert(arg);
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void insertDoubleArg() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.insert(arg);
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void insertOpEmpty() {
    tree.insert(op);
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void insertDoubleOp() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(op);
  }
  
  @Test
  public void insertSingleUnary() {
    tree.insert(uOp);
    tree.insert(arg);
    assertThat(tree.size(), is(1));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void insertOpOverOp() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.size(), is(5));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void insertUnaryThenOp() {
    tree.insert(uOp);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void insertInlineUnary() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(uOp);
    tree.insert(arg);
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void priorityInsert() {
    Operator op1 = mock(Operator.class);
    Operator op2 = mock(Operator.class);
    when(op1.priority()).thenReturn(1);
    when(op1.getSymbol()).thenReturn("1");
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("2");
    Argument arg1 = mock(Argument.class);
    Argument arg2 = mock(Argument.class);
    Argument arg3 = mock(Argument.class);
    when(arg1.getValue()).thenReturn("a");
    when(arg2.getValue()).thenReturn("b");
    when(arg3.getValue()).thenReturn("c");
    
    tree.insert(arg1);
    tree.insert(op1);
    tree.insert(arg2);
    tree.insert(op2);
    tree.insert(arg3);
    assertThat(tree.size(), is(5));
    Assert.assertTrue(tree.valid());
    
    Node expected = newTree(newArgNode(arg1), simpleTree(arg2, arg3, op2), op1);
    assertThat(tree, isSame(expected));
  }
  
  @Test
  public void increasingPriorityInsert() {
    Operator op1 = mock(Operator.class);
    Operator op2 = mock(Operator.class);
    Operator op3 = mock(Operator.class);
    when(op1.priority()).thenReturn(1);
    when(op1.getSymbol()).thenReturn("1");
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("2");
    when(op3.priority()).thenReturn(3);
    when(op3.getSymbol()).thenReturn("3");
    Argument arg1 = mock(Argument.class);
    Argument arg2 = mock(Argument.class);
    Argument arg3 = mock(Argument.class);
    Argument arg4 = mock(Argument.class);
    when(arg1.getValue()).thenReturn("a");
    when(arg2.getValue()).thenReturn("b");
    when(arg3.getValue()).thenReturn("c");
    when(arg4.getValue()).thenReturn("d");
    
    tree.insert(arg1);
    tree.insert(op1);
    tree.insert(arg2);
    tree.insert(op2);
    tree.insert(arg3);
    tree.insert(op3);
    tree.insert(arg4);
    assertThat(tree.size(), is(7));
    Assert.assertTrue(tree.valid());
    
    Node expected = newTree(newArgNode(arg1), newTree(newArgNode(arg2),
        simpleTree(arg3, arg4, op3), op2), op1);
    assertThat(tree, isSame(expected));
  }
  
  @Test
  public void invalidEmpty() {
    assertThat(tree.size(), is(0));
    Assert.assertFalse(tree.valid());
  }
  
  @Test
  public void validSingleArg() {
    tree.insert(arg);
    assertThat(tree.size(), is(1));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void invalidHangingOp() {
    tree.insert(arg);
    tree.insert(op);
    Assert.assertFalse(tree.valid());
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void insertInvalidTree() {
    tree.insert(new EvalTree());
  }
  
  @Test
  public void insertTree() {
    when(op.getSymbol()).thenReturn("op");
    Argument arg1 = mock(Argument.class);
    Argument arg2 = mock(Argument.class);
    Argument arg3 = mock(Argument.class);
    when(arg1.getValue()).thenReturn("a");
    when(arg2.getValue()).thenReturn("b");
    when(arg3.getValue()).thenReturn("c");
    
    tree.insert(arg1);
    tree.insert(op);
    EvalTree tree2 = new EvalTree();
    tree2.insert(arg2);
    tree2.insert(op);
    tree2.insert(arg3);
    tree.insert(tree2);
    assertThat(tree.size(), is(5));
    Assert.assertTrue(tree.valid());
    
    Node expected = newTree(newArgNode(arg1), simpleTree(arg2, arg3, op), op);
    assertThat(tree, isSame(expected));
  }
  
  @Test
  public void insertTreeEmptyUnary() {
    EvalTree test = new EvalTree();
    test.insert(arg);
    test.insert(op);
    test.insert(arg);
    tree.insert(uOp);
    tree.insert(test);
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void insertTreeUnary() {
    when(op.getSymbol()).thenReturn("op");
    when(uOp.getSymbol()).thenReturn("uOp ");
    Argument arg1 = mock(Argument.class);
    Argument arg2 = mock(Argument.class);
    Argument arg3 = mock(Argument.class);
    when(arg1.getValue()).thenReturn("a");
    when(arg2.getValue()).thenReturn("b");
    when(arg3.getValue()).thenReturn("c");
    
    tree.insert(arg1);
    tree.insert(op);
    tree.insert(uOp);
    EvalTree tree2 = new EvalTree();
    tree2.insert(arg2);
    tree2.insert(op);
    tree2.insert(arg3);
    tree.insert(tree2);
    assertThat(tree.size(), is(5));
    Assert.assertTrue(tree.valid());
    
    Node expected = newTree(newArgNode(arg1), simpleTree(arg2, arg3, op), op);
    expected.right.uOp = uOp;
    assertThat(tree, isSame(expected));
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void insertTreeInvalid() {
    tree.insert(arg);
    EvalTree test = new EvalTree();
    test.insert(arg);
    tree.insert(test);
  }
  
  @Test
  public void avoidHeapifySubTree() {
    when(op.getSymbol()).thenReturn("op");
    Argument arg1 = mock(Argument.class);
    Argument arg2 = mock(Argument.class);
    Argument arg3 = mock(Argument.class);
    when(arg1.getValue()).thenReturn("a");
    when(arg2.getValue()).thenReturn("b");
    when(arg3.getValue()).thenReturn("c");
    
    EvalTree test = new EvalTree();
    test.insert(arg1);
    test.insert(op);
    test.insert(arg2);
    tree.insert(test);
    tree.insert(op);
    tree.insert(arg3);
    assertThat(tree.size(), is(5));
    Assert.assertTrue(tree.valid());

    Node expected = newTree(simpleTree(arg1, arg2, op), newArgNode(arg3), op);
    assertThat(tree, isSame(expected));
  }
  
  @Test(expected = NotEvaluableException.class)
  public void evaluateInvalid() {
    tree.evaluate();
  }
  
  @Test
  public void evaluateSingleArg() {
    tree.insert(arg);
    assertThat(tree.size(), is(1));
    assertThat(tree.evaluate(), is(arg));
  }
  
  @Test
  public void evaluateSingleArgUnary() {
    Argument negArg = mock(Argument.class);
    when(uOp.evaluate(arg)).thenReturn(negArg);
    tree.insert(uOp);
    tree.insert(arg);
    assertThat(tree.size(), is(1));
    assertThat(tree.evaluate(), is(negArg));
    verify(uOp, times(1)).evaluate(arg);
  }
  
  @Test(expected = NotEvaluableException.class)
  public void notEvaluableUnary() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    tree.insert(uOp);
    tree.insert(nArg);
    assertThat(tree.size(), is(1));
    tree.evaluate();
  }
  
  @Test
  public void evaluableUnary() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    when(nArg.isEvaluable()).thenReturn(true);
    when(uOp.evaluate(nArg)).thenReturn(arg);
    tree.insert(uOp);
    tree.insert(nArg);
    assertThat(tree.size(), is(1));
    assertThat(tree.evaluate(), is(arg));
    verify(uOp, times(1)).evaluate(nArg);
  }
  
  @Test
  public void evaluableOp() {
    Argument newArg = mock(Argument.class);
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    when(nArg.isEvaluable()).thenReturn(true);
    when(nArg.convert()).thenReturn(newArg);
    when(op.evaluate(newArg, newArg)).thenReturn(arg);
    tree.insert(nArg);
    tree.insert(op);
    tree.insert(nArg);
    assertThat(tree.size(), is(3));
    assertThat(tree.evaluate(), is(arg));
    assertThat(tree.size(), is(1));
    verify(op, times(1)).evaluate(newArg, newArg);
  }
  
  @Test(expected = NotEvaluableException.class)
  public void notEvaluableOpLeft() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    tree.insert(nArg);
    tree.insert(op);
    tree.insert(nArg);
    assertThat(tree.size(), is(3));
    tree.evaluate();
  }
  
  @Test(expected = NotEvaluableException.class)
  public void notEvaluableOpRight() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(nArg);
    assertThat(tree.size(), is(3));
    tree.evaluate();
  }
  
  @Test(expected = NotEvaluableException.class)
  public void notEvaluableVariable() {
    VariableArgument vArg = mock(VariableArgument.class);
    when(vArg.toString()).thenReturn("var");
    tree.insert(arg);
    tree.insert(op);
    tree.insert(vArg);
    assertThat(tree.size(), is(3));
    tree.evaluate();
  }
  
  @Test
  public void evaluate() {
    Argument newArg = mock(Argument.class);
    when(op.evaluate(arg, arg)).thenReturn(newArg);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.size(), is(3));
    assertThat(tree.evaluate(), is(newArg));
    verify(op, times(1)).evaluate(arg, arg);
  }
  
  @Test
  public void evaluate2() {
    Argument negArg = mock(Argument.class);
    Argument newArg = mock(Argument.class);
    when(op.evaluate(arg, arg)).thenReturn(newArg);
    when(uOp.evaluate(newArg)).thenReturn(negArg);
    EvalTree test = new EvalTree();
    test.insert(arg);
    test.insert(op);
    test.insert(arg);
    tree.insert(uOp);
    tree.insert(test);
    assertThat(tree.size(), is(3));
    assertThat(tree.evaluate(), is(negArg));
    verify(op, times(1)).evaluate(arg, arg);
    verify(uOp, times(1)).evaluate(newArg);
  }
  
  @Test
  public void evaluate3() {
    Argument newArg = mock(Argument.class);
    when(op.evaluate(arg, arg)).thenReturn(newArg);
    when(uOp.evaluate(newArg)).thenReturn(null);
    EvalTree test = new EvalTree();
    test.insert(arg);
    test.insert(op);
    test.insert(arg);
    tree.insert(uOp);
    tree.insert(test);
    assertThat(tree.size(), is(3));
    assertThat(tree.evaluate(), is(nullValue()));
    verify(op, times(1)).evaluate(arg, arg);
    verify(uOp, times(1)).evaluate(newArg);
  }
  
  @Test
  public void evaluateNotEvaluableConvert() {
    Argument newArg = mock(Argument.class);
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    when(nArg.isEvaluable()).thenReturn(true);
    when(nArg.convert()).thenReturn(newArg);
    
    tree.insert(nArg);
    assertThat(tree.size(), is(1));
    assertThat(tree.evaluate(), is(newArg));
    verify(nArg, times(1)).convert();
  }
  
  @Test
  public void heapifyValidTest() {
    Operator op1 = mock(Operator.class);
    Operator op2 = mock(Operator.class);
    when(op1.priority()).thenReturn(1);
    when(op2.priority()).thenReturn(2);
    
    tree.insert(arg);
    tree.insert(op1);
    tree.insert(arg);
    tree.insert(op2);
    Assert.assertFalse(tree.valid());
  }
  
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void equalsTest() {
    Assert.assertFalse(tree.equals("test"));
    Assert.assertTrue(tree.equals(tree));
    Assert.assertTrue(tree.equals(new Node()));
    EvalTree test = new EvalTree();
    Assert.assertTrue(tree.equals(test));
    test.insert(arg);
    Assert.assertFalse(tree.equals(test));
    Assert.assertFalse(test.equals(tree));
    tree.insert(arg);
    Assert.assertTrue(tree.equals(test));
    test.insert(op);
    Assert.assertFalse(tree.equals(test));
    Assert.assertFalse(test.equals(tree));
    tree.insert(op);
    Assert.assertTrue(tree.equals(test));
    test.insert(uOp);
    test.insert(arg);
    Assert.assertFalse(tree.equals(test));
    Assert.assertFalse(test.equals(tree));
    tree.insert(arg);
    Assert.assertFalse(tree.equals(test));
    Assert.assertFalse(test.equals(tree));
  }
  
  @Test
  public void equals2() {
    Operator op2 = mock(Operator.class);
    when(op.priority()).thenReturn(1);
    when(op2.priority()).thenReturn(2);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.insert(op2);
    tree.insert(arg);
    EvalTree other = new EvalTree();
    other.insert(arg);
    other.insert(op);
    other.insert(arg);
    other.insert(op);
    other.insert(arg);
    Assert.assertFalse(tree.equals(other));
    Assert.assertFalse(other.equals(tree));
    EvalTree other2 = new EvalTree();
    other2.insert(arg);
    other2.insert(op);
    other2.insert(arg);
    other2.insert(op2);
    other2.insert(arg);
    Assert.assertTrue(tree.equals(other2));
    Assert.assertTrue(other2.equals(tree));
  }
  
  @Test
  public void toStringTest() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.toString(), is("\"arg\" + \"arg\""));
  }
  
  @Test
  public void toStringUOp() {
    UnaryOperator uOp = mock(UnaryOperator.class);
    when(uOp.getSymbol()).thenReturn("-");
    tree.insert(arg);
    tree.insert(op);
    tree.insert(uOp);
    tree.insert(arg);
    assertThat(tree.toString(), is("\"arg\" + -\"arg\""));
  }
  
  @Test
  public void toStringDepth() {
    Operator op2 = mock(Operator.class);
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("*");
    tree.insert(arg);
    tree.insert(op2);
    EvalTree tree2 = new EvalTree();
    tree2.insert(arg);
    tree2.insert(op);
    tree2.insert(arg);
    tree.insert(tree2);
    assertThat(tree.toString(), is("\"arg\" * (\"arg\" + \"arg\")"));
  }
  
  @Test
  public void toStringDepthUOp() {
    Operator op2 = mock(Operator.class);
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("*");
    UnaryOperator uOp = mock(UnaryOperator.class);
    when(uOp.getSymbol()).thenReturn("-");
    tree.insert(arg);
    tree.insert(op2);
    EvalTree tree2 = new EvalTree();
    tree2.insert(arg);
    tree2.insert(op);
    tree2.insert(arg);
    tree.insert(uOp);
    tree.insert(tree2);
    assertThat(tree.toString(), is("\"arg\" * -(\"arg\" + \"arg\")"));
  }
  
  @Test
  public void toStringVariableNotInMap() {
    VariableArgument vArg = mock(VariableArgument.class);
    when(vArg.toString()).thenReturn("null");
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.toString(), is("null + \"arg\""));
  }
  
  @Test
  public void toStringVariable() {
    VariableArgument vArg = mock(VariableArgument.class);
    when(vArg.toString()).thenReturn("x");
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.toString(), is("x + \"arg\""));
  }
  
  @Test
  public void toStringVariableSame() {
    VariableArgument vArg = mock(VariableArgument.class);
    when(vArg.toString()).thenReturn("x");
    tree.insert(vArg);
    tree.insert(op);
    tree.insert(vArg);
    assertThat(tree.toString(), is("x + x"));
  }
  
  @Test
  public void toStringOpFlippedPriority() {
    Operator op2 = mock(Operator.class);
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("*");
    EvalTree main = new EvalTree();
    EvalTree sub = new EvalTree();
    sub.insert(arg);
    sub.insert(op);
    sub.insert(arg);
    main.insert(sub);
    main.insert(op2);
    main.insert(arg);
    assertThat(main.toString(), is("(\"arg\" + \"arg\") * \"arg\""));
  }
  
  @Test
  public void toStringOpNormalPriority() {
    Operator op2 = mock(Operator.class);
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("*");
    EvalTree main = new EvalTree();
    EvalTree sub = new EvalTree();
    sub.insert(arg);
    sub.insert(op2);
    sub.insert(arg);
    main.insert(sub);
    main.insert(op);
    main.insert(arg);
    assertThat(main.toString(), is("\"arg\" * \"arg\" + \"arg\""));
  }
  
  @Test
  public void toStringHeapifyPriority() {
    Operator op2 = mock(Operator.class);
    when(op2.priority()).thenReturn(2);
    when(op2.getSymbol()).thenReturn("*");
    EvalTree main = new EvalTree();
    main.insert(arg);
    main.insert(op);
    main.insert(arg);
    main.insert(op2);
    main.insert(arg);
    assertThat(main.toString(), is("\"arg\" + \"arg\" * \"arg\""));
  }
  
  @Test
  public void copyConstructor() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    EvalTree tree2 = new EvalTree(tree);
    assertThat(tree2, isSame(tree.getRoot()));
  }
  
  @Test
  public void precomputeInvalid() {
    assertThat(tree.precompute(), is(false));
  }
  
  @Test
  public void precompute() {
    Argument newArg = mock(Argument.class);
    when(op.evaluate(arg, arg)).thenReturn(newArg);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.size(), is(3));
    assertThat(tree.precompute(), is(true));
    verify(op, times(1)).evaluate(arg, arg);
    assertThat(tree.size(), is(1));
  }
  
  @Test
  public void notPrecomputeOpLeft() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    tree.insert(nArg);
    tree.insert(op);
    tree.insert(nArg);
    assertThat(tree.size(), is(3));
    assertThat(tree.precompute(), is(false));
  }
  
  @Test
  public void notPrecomputeOpRight() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    tree.insert(arg);
    tree.insert(op);
    tree.insert(nArg);
    assertThat(tree.size(), is(3));
    assertThat(tree.precompute(), is(false));
    assertThat(tree.size(), is(3));
  }
  
  @Test
  public void notPrecomputeUnary() {
    NotEvaluableArg nArg = mock(NotEvaluableArg.class);
    tree.insert(uOp);
    tree.insert(nArg);
    assertThat(tree.size(), is(1));
    assertThat(tree.precompute(), is(false));
    assertThat(tree.size(), is(1));
  }
  
  private Node newArgNode(Argument arg) {
    Node out = new Node();
    out.value = arg;
    return out;
  }
  
  private Node newTree(Node left, Node right, Operator op) {
    Node out = new Node();
    out.op = op;
    out.left = left;
    out.right = right;
    return out;
  }
  
  private Node simpleTree(Argument left, Argument right, Operator op) {
    return newTree(newArgNode(left), newArgNode(right), op);
  }
  
  public static Matcher<EvalTree> isSame(Node expected) {
    return new BaseMatcher<EvalTree>() {

      @SuppressWarnings("unlikely-arg-type")
      @Override
      public boolean matches(Object item) {
        EvalTree test = (EvalTree)item;
        return test.equals(expected);
      }
      @Override
      public void describeTo(Description description) {
        description.appendText("\n" + EvalTreePrinter.print(expected));
      }
    };
  }
  
  private static abstract class NotEvaluableArg extends Argument implements NotEvaluable { }
}
