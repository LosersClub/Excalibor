package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.VariableArgument;
import com.github.losersclub.excalibor.argument.primitives.BooleanArgument;
import com.github.losersclub.excalibor.argument.primitives.IntArgument;

public class BooleanArgumentTest {
  private BooleanArgument boolArgFalse = new BooleanArgument();
  private BooleanArgument boolArgTrue = new BooleanArgument(true);

  @Test
  public void testParse() throws Exception {
    Assert.assertNull(boolArgTrue.parse("blargh"));
    Assert.assertNull(boolArgTrue.parse(""));
    Argument boolArgParsed = boolArgTrue.parse("true");
    Assert.assertTrue(boolArgParsed instanceof BooleanArgument
        && (boolean)boolArgParsed.getValue());
    boolArgParsed = boolArgTrue.parse("false");
    Assert.assertTrue(boolArgParsed instanceof BooleanArgument
        && !((boolean)boolArgParsed.getValue()));
  }
  
  @Test
  public void testToString() {
    Assert.assertTrue(boolArgTrue.toString().equals("true"));
  }
  
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testInternalEquals() {
    Assert.assertTrue(boolArgTrue.equals(true));
    Assert.assertTrue(boolArgTrue.equals((Object)boolArgTrue));
    Assert.assertFalse(boolArgTrue.equals((Object)boolArgFalse));
  }
  
  @Test
  public void testConvert() {
    VariableArgument vArg = Mockito.mock(VariableArgument.class);
    Mockito.when(vArg.getValue()).thenReturn(true);
    Argument arg = boolArgTrue.convert(vArg);
    Assert.assertTrue(arg instanceof BooleanArgument && (boolean)arg.getValue());
    Mockito.when(vArg.getValue()).thenReturn(null);
    Assert.assertTrue(boolArgTrue.convert(vArg) == null);
    Mockito.when(vArg.getValue()).thenReturn(new Object());
    Assert.assertTrue(boolArgTrue.convert(vArg) == null);
  }
  
  @Test
  public void testBuild() {
    Argument built = boolArgTrue.build(true);
    Assert.assertTrue(built instanceof BooleanArgument && (boolean)built.getValue());
  }

  @Test
  public void testNot() throws Exception {
    Assert.assertTrue((boolean)boolArgFalse.not().getValue());
    Assert.assertFalse((boolean)boolArgTrue.not().getValue());
  }

  @Test(expected = InvalidExpressionException.class)
  public void testComparesInvalidRHS() throws Exception {
    IntArgument intArg = new IntArgument();
    boolArgTrue.equals(intArg);
  }

  @Test
  public void testEquals() throws Exception {
    Assert.assertFalse((boolean)boolArgTrue.equals(boolArgFalse).getValue());
    Assert.assertTrue((boolean)boolArgTrue.equals(boolArgTrue).getValue());
  }

  @Test
  public void testAnd() throws Exception {
    Assert.assertFalse((boolean)boolArgTrue.and(boolArgFalse).getValue());
    Assert.assertFalse((boolean)boolArgFalse.and(boolArgFalse).getValue());
    Assert.assertTrue((boolean)boolArgTrue.and(boolArgTrue).getValue());
  }

  @Test
  public void testOr() throws Exception {
    Assert.assertTrue((boolean)boolArgTrue.or(boolArgFalse).getValue());
    Assert.assertTrue((boolean)boolArgTrue.or(boolArgTrue).getValue());
    Assert.assertFalse((boolean)boolArgFalse.or(boolArgFalse).getValue());
    Assert.assertTrue((boolean)boolArgFalse.or(boolArgTrue).getValue());
  }

  @Test
  public void testXor() throws Exception {
    Assert.assertTrue((boolean)boolArgTrue.xor(boolArgFalse).getValue());
    Assert.assertFalse((boolean)boolArgTrue.xor(boolArgTrue).getValue());
  }

  @Test
  public void testNotEquals() throws Exception {
    Assert.assertTrue((boolean)boolArgTrue.notEquals(boolArgFalse).getValue());
  }
}