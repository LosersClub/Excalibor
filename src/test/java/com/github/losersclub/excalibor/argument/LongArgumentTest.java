package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.primitives.IntArgument;
import com.github.losersclub.excalibor.argument.primitives.LongArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;


public class LongArgumentTest {
  private LongArgument longArgOne = new LongArgument();
  private LongArgument longArgTwo = new LongArgument(3L);
  private LongArgument longArgThree = new LongArgument(2L);
  
  @Test
  public void testParse() {
    Assert.assertNull(longArgOne.parse("'1.02'"));
    Assert.assertNull(longArgOne.parse("a12"));
    Assert.assertNull(longArgOne.parse("1234f"));
    Assert.assertNull(longArgOne.parse("1.23.4d"));
    Assert.assertNull(longArgOne.parse("12345"));
    Assert.assertNull(longArgOne.parse("L"));
    Assert.assertNull(longArgOne.parse(""));
    Argument longArgParsed = longArgOne.parse("123l");
    Assert.assertTrue(longArgParsed instanceof LongArgument
        && (long)longArgParsed.getValue() == 123L);
    longArgParsed = longArgOne.parse("456L");
    Assert.assertTrue(longArgParsed instanceof LongArgument
        && (long)longArgParsed.getValue() == 456L);
    longArgParsed = longArgOne.parse("123456789101112L");
    Assert.assertTrue(longArgParsed instanceof LongArgument
        && (long)longArgParsed.getValue() == 123456789101112L);
  }
  
  @Test
  public void testBuild() {
    Argument built = longArgOne.build((long)6L);
    Assert.assertTrue(built instanceof LongArgument && (long)built.getValue() == 6L);
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void testBuildCast() {
    longArgOne.build("test");
  }

  @Test (expected = InvalidExpressionException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    longArgOne.add(stringArg);
  }

  @Test
  public void testNonLongRHS() {
   IntArgument intArg = new IntArgument(4);
   Argument longArgParsed = longArgTwo.add(intArg);
   Assert.assertTrue(longArgParsed instanceof LongArgument
       && (long)longArgParsed.getValue() == 7L);
  }

  @Test
  public void testAdd() {
    Assert.assertTrue(5L == (long)longArgTwo.add(longArgThree).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertTrue(-1L == (long)longArgThree.subtract(longArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertTrue(6L == (long)longArgThree.multiply(longArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertTrue(1L == (long)longArgTwo.divide(longArgThree).getValue());
  }

  @Test
  public void testModulo() {
    Assert.assertTrue(2L == (long)longArgThree.modulo(longArgTwo).getValue());
    Assert.assertTrue(1L == (long)longArgTwo.modulo(longArgThree).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)longArgOne.lessThan(longArgTwo).getValue());
    Assert.assertFalse((boolean)longArgTwo.lessThan(longArgOne).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)longArgTwo.greaterThan(longArgOne).getValue());
    Assert.assertFalse((boolean)longArgOne.greaterThan(longArgTwo).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)longArgTwo.equals(longArgTwo).getValue());
    Assert.assertFalse((boolean)longArgTwo.equals(longArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((0 - (long)longArgTwo.getValue()) == (long)longArgTwo.negate().getValue());
  }

}