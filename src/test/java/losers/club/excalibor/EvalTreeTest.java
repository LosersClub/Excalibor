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
import losers.club.excalibor.operator.Operator;
import losers.club.excalibor.operator.UnaryOperator;

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
    Node expected = new Node();
    expected.op = op;
    Node leftTree = new Node();
    leftTree.op = op;
    expected.left = leftTree;
    Node rightTree = new Node();
    rightTree.op = op;
    expected.right = rightTree;
    leftTree.left = new Node();
    leftTree.left.value = arg;
    leftTree.right = new Node();
    leftTree.right.value = arg;
    rightTree.left = new Node();
    rightTree.left.value = arg;
    rightTree.right = new Node();
    rightTree.right.value = arg;
    
    
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.insert(op);
    tree.openBracket();
    tree.insert(arg);
    tree.insert(op);
    tree.insert(arg);
    tree.closeBracket();
    assertThat(tree, isSame(expected));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void heapifyInvalid() {
    tree.insert(arg);
    tree.insert(op);
    tree.heapify();
  }
  
  private Matcher<EvalTree> isSame(Node expected) {
    return new BaseMatcher<EvalTree>() {

      @Override
      public boolean matches(Object item) {
        EvalTree test = (EvalTree)item;
        return EvalTreeTest.equals(test.root, expected);
      }
      @Override
      public void describeTo(Description description) {
        description.appendText("Trees were different");
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
