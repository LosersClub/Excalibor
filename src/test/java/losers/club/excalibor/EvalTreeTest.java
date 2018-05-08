package losers.club.excalibor;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import losers.club.excalibor.EvalTree.Node;
import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.IntArgument;
import losers.club.excalibor.argument.primitives.StringArgument;
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;
import losers.club.excalibor.operator.primitives.AddOperator;
import losers.club.excalibor.operator.primitives.AndOperator;
import losers.club.excalibor.operator.primitives.DivideOperator;
import losers.club.excalibor.operator.primitives.GreaterThanOperator;
import losers.club.excalibor.operator.primitives.LessThanEqOperator;
import losers.club.excalibor.operator.primitives.ModuloOperator;
import losers.club.excalibor.operator.primitives.MultiplyOperator;
import losers.club.excalibor.operator.primitives.NegateOperator;

@RunWith(MockitoJUnitRunner.class)
public class EvalTreeTest {
  @Mock private Argument arg;
  @Mock private Operator op;
  @Mock private UnaryOperator uOp;
  EvalTree tree = new EvalTree();
  
  @Test
  public void singleArgTree() {
    tree.insert(arg);
    Assert.assertTrue(tree.valid());
    assertThat(tree.size(), is(1));
  }
  
  @Test
  public void insertUnary() {
    tree.insert(arg, uOp);
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
  
  @Test(expected = IllegalArgumentException.class)
  public void insertNoOp() {
    tree.insert(arg);
    tree.insert(arg);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void insertDoubleArg() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.insert(arg);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void insertOpEmpty() {
    tree.insert(op);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void insertDoubleOp() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(op);
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
    tree.insert(arg, uOp);
    tree.insert(op);
    tree.insert(arg);
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void openBracketOneArg() {
    tree.insert(arg);
    tree.openBracket();
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void openBracketAfterSecondArg() {
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.openBracket();
  }
  
  @Test
  public void openBracketEmpty() {
    tree.openBracket();
    tree.closeBracket();
    assertThat(tree.size(), is(0));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void openBracketAfterOp() {
    tree.insert(arg);
    tree.insert(op);
    tree.openBracket();
    tree.insert(arg);
    tree.closeBracket();
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void closeBracketRoot() {
    tree.closeBracket();
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void closeBracketNoOpen() {
    tree.insert(arg);
    tree.closeBracket();
  }
  
  @Test
  public void validEmpty() {
    assertThat(tree.size(), is(0));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void validSingleArg() {
    tree.insert(arg);
    assertThat(tree.size(), is(1));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void validMultiple() {
    tree.openBracket();
    tree.insert(arg);
    tree.insert(op);
    tree.openBracket();
    tree.insert(arg);
    tree.closeBracket();
    tree.closeBracket();
    assertThat(tree.size(), is(3));
    Assert.assertTrue(tree.valid());
  }
  
  @Test
  public void invalidOpenCountBracket() {
    tree.openBracket();
    Assert.assertFalse(tree.valid());
  }
  
  @Test
  public void invalidOpenBracket() {
    tree.insert(arg);
    tree.insert(op);
    tree.openBracket();
    tree.insert(arg);
    Assert.assertFalse(tree.valid());
  }
  
  @Test
  public void invalidHangingOp() {
    tree.insert(arg);
    tree.insert(op);
    Assert.assertFalse(tree.valid());
  }
  
  @Test
  public void inlineBracket() {
    StringArgument sArg = new StringArgument("arg");
    AddOperator plus = new AddOperator();
    
    Node expected = new Node();
    expected.op = plus;
    Node leftTree = new Node();
    leftTree.op = plus;
    expected.left = leftTree;
    Node rightTree = new Node();
    rightTree.op = plus;
    expected.right = rightTree;
    leftTree.left = new Node();
    leftTree.left.value = sArg;
    leftTree.right = new Node();
    leftTree.right.value = sArg;
    rightTree.left = new Node();
    rightTree.left.value = sArg;
    rightTree.right = new Node();
    rightTree.right.value = sArg;
    
    
    tree.insert(sArg);
    tree.insert(plus);
    tree.insert(sArg);
    tree.insert(plus);
    tree.openBracket();
    tree.insert(sArg);
    tree.insert(plus);
    tree.insert(sArg);
    tree.closeBracket();
    assertThat(tree, isSame(expected));
  }
  
  @Test
  public void priorityTest() throws NotEvaluatableException {
    IntArgument i = new IntArgument(1);
    MultiplyOperator mult = new MultiplyOperator();
    DivideOperator div = new DivideOperator();
    NegateOperator neg = new NegateOperator();
    AddOperator add = new AddOperator();
    
    Node expected = new Node();
    expected.op = add;
    expected.left = new Node();
    expected.left.value = i;
    Node subTree = new Node();
    expected.right = subTree;
    subTree.op = div;
    Node leftTree = new Node();
    subTree.left = leftTree;
    leftTree.op = mult;
    leftTree.left = new Node();
    leftTree.left.value = i;
    leftTree.right = new Node();
    leftTree.right.value = i;
    Node rightTree = new Node();
    subTree.right = rightTree;
    rightTree.op = neg;
    rightTree.left = new Node();
    rightTree.left.value = i;
    rightTree.right = new Node();
    rightTree.right.value = i;
    
    tree.insert(i,neg);
    tree.insert(add);
    tree.insert(i);
    tree.insert(mult);
    tree.insert(i);
    tree.insert(new ModuloOperator());
    tree.openBracket();
    tree.insert(i);
    tree.insert(neg);
    tree.insert(i);
    tree.closeBracket();
    assertThat(tree, isSame(expected));
//    System.out.println(EvalTreePrinter.print(tree.root));
//    System.out.println(tree.evaluate().getValue());
  }
  
//   @Test
//   public void priorityTest2() throws NotEvaluatableException {
//     tree.insert(new IntArgument(1));
//     tree.insert(new AddOperator());
//     tree.insert(new IntArgument(4));
//     tree.insert(new LessThanEqOperator());
//     tree.insert(new IntArgument(0));
//     tree.insert(new AndOperator());
//     tree.insert(new IntArgument(1));
//     tree.insert(new AddOperator());
//     tree.insert(new IntArgument(2));
//     tree.insert(new GreaterThanOperator());
//     tree.insert(new IntArgument(0));
//     System.out.println(EvalTreePrinter.print(tree.root));
//     Argument out = tree.evalute();
//     System.out.println(out.getValue());
//   }

  private Matcher<EvalTree> isSame(Node expected) {
    return new BaseMatcher<EvalTree>() {

      @Override
      public boolean matches(Object item) {
        EvalTree test = (EvalTree)item;
        return EvalTreeTest.equals(test.root, expected);
      }
      @Override
      public void describeTo(Description description) {
        description.appendText("\n" + EvalTreePrinter.print(expected));
      }
      @Override
      public void describeMismatch(Object item, Description description) {
        description.appendText("\n" + EvalTreePrinter.print(((EvalTree)item).root));
      }
    };
  }
  
  private static boolean equals(Node leftTree, Node rightTree) {
    if ((leftTree == null && rightTree != null) ||
        (leftTree != null && rightTree == null)) {
      return false;
    }
    
    if (leftTree == null && rightTree == null) {
      return true;
    }
    
    if (leftTree.op != rightTree.op || leftTree.value != rightTree.value) {
      return false;
    }
    
    return equals(leftTree.left, rightTree.left) && equals(leftTree.right, rightTree.right);
  }
}
