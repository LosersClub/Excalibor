package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.primitives.FloatArgument;
import com.github.losersclub.excalibor.argument.primitives.IntArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;


public class FloatArgumentTest {
  private FloatArgument floatArgOne = new FloatArgument();
  private FloatArgument floatArgTwo = new FloatArgument(1.5f);
  private FloatArgument floatArgThree = new FloatArgument(2f);

  @Test
  public void testParse() {
    Assert.assertNull(floatArgOne.parse("'1.02f'"));
    Assert.assertNull(floatArgOne.parse("a12"));
    Assert.assertNull(floatArgOne.parse("1234F"));
    Assert.assertNull(floatArgOne.parse("12.34g"));
    Assert.assertNull(floatArgOne.parse("1.23.4f"));
    Assert.assertNull(floatArgOne.parse(""));
    Argument floatArgParsed = floatArgOne.parse("1.23f");
    Assert.assertTrue(floatArgParsed instanceof FloatArgument
        && (float)floatArgParsed.getValue() == 1.23f);
    floatArgParsed = floatArgOne.parse(".456F");
    Assert.assertTrue(floatArgParsed instanceof FloatArgument
        && (float)floatArgParsed.getValue() == 0.456f);
    floatArgParsed = floatArgOne.parse("81.46F");
    Assert.assertTrue(floatArgParsed instanceof FloatArgument
        && (float)floatArgParsed.getValue() == 81.46f);
  }
  
  @Test
  public void testBuild() {
    Argument built = floatArgOne.build((float)2.4);
    Assert.assertTrue(built instanceof FloatArgument && (float)built.getValue() == 2.4f);
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void testBuildCast() {
    floatArgOne.build("test");
  }

  @Test (expected = InvalidExpressionException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    floatArgOne.add(stringArg);
  }

  @Test
  public void testNonFloatRHS() {
   IntArgument intArg = new IntArgument(4);
   Argument floatArgParsed = floatArgTwo.add(intArg);
   Assert.assertTrue(floatArgParsed instanceof FloatArgument
       && (float)floatArgParsed.getValue() == 5.5f);
  }

  @Test
  public void testAdd() {
    Assert.assertTrue(1.5f == (float)floatArgOne.add(floatArgTwo).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertTrue(-1.5f == (float)floatArgOne.subtract(floatArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertTrue(3.0f == (float)floatArgThree.multiply(floatArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertTrue(0.75f == (float)floatArgTwo.divide(floatArgThree).getValue());
  }

  @Test
  public void testModulo() {
    Assert.assertTrue(0.0f == (float)floatArgOne.modulo(floatArgTwo).getValue());
    Assert.assertTrue(0.5f == (float)floatArgThree.modulo(floatArgTwo).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)floatArgOne.lessThan(floatArgTwo).getValue());
    Assert.assertFalse((boolean)floatArgTwo.lessThan(floatArgOne).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)floatArgTwo.greaterThan(floatArgOne).getValue());
    Assert.assertFalse((boolean)floatArgOne.greaterThan(floatArgTwo).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)floatArgTwo.equals(floatArgTwo).getValue());
    Assert.assertFalse((boolean)floatArgTwo.equals(floatArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((0 - (float)floatArgTwo.getValue()) ==
        (float)floatArgTwo.negate().getValue());
  }

}