package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.primitives.DoubleArgument;
import com.github.losersclub.excalibor.argument.primitives.ShortArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;


public class ShortArgumentTest {
  private ShortArgument shortArgOne = new ShortArgument();
  private ShortArgument shortArgTwo = new ShortArgument((short)5);
  private ShortArgument shortArgThree = new ShortArgument((short)2);

  @Test
  public void testParse() {
    Assert.assertNull(shortArgOne.parse("'1.02'"));
    Assert.assertNull(shortArgOne.parse("a12"));
    Assert.assertNull(shortArgOne.parse("1234"));
    Assert.assertNull(shortArgOne.parse("1.23.4d"));
    Assert.assertNull(shortArgOne.parse("32768S"));
    Assert.assertNull(shortArgOne.parse(""));
    Argument shortArgParsed = shortArgOne.parse("123s");
    Assert.assertTrue(shortArgParsed instanceof ShortArgument
        && (short)shortArgParsed.getValue() == (short)123);
    shortArgParsed = shortArgOne.parse("456s");
    Assert.assertTrue(shortArgParsed instanceof ShortArgument
        && (short)shortArgParsed.getValue() == (short)456);
    shortArgParsed = shortArgOne.parse("32767S");
    Assert.assertTrue(shortArgParsed instanceof ShortArgument
        && (short)shortArgParsed.getValue() == (short)32767);
  }
  
  @Test
  public void testBuild() {
    Argument built = shortArgOne.build((short)2);
    Assert.assertTrue(built instanceof ShortArgument && (short)built.getValue() == 2);
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void testBuildCast() {
    shortArgOne.build("test");
  }

  @Test (expected = InvalidExpressionException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    shortArgOne.add(stringArg);
  }

  @Test
  public void testNonShortRHS() {
   DoubleArgument doubleArg = new DoubleArgument(4.00);
   Argument shortArgParsed = shortArgTwo.add(doubleArg);
   Assert.assertTrue(shortArgParsed instanceof DoubleArgument
       && (double)shortArgParsed.getValue() == 9);
  }

  @Test
  public void testAdd() {
    Assert.assertTrue((short)7 == (short)shortArgTwo.add(shortArgThree).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertTrue((short)-3 == (short)shortArgThree.subtract(shortArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertTrue((short)10 == (short)shortArgThree.multiply(shortArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertTrue((short)2 == (short)shortArgTwo.divide(shortArgThree).getValue());
  }

  @Test
  public void testModulo() {
    Assert.assertTrue((short)2 == (short)shortArgThree.modulo(shortArgTwo).getValue());
    Assert.assertTrue((short)1 == (short)shortArgTwo.modulo(shortArgThree).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)shortArgOne.lessThan(shortArgTwo).getValue());
    Assert.assertFalse((boolean)shortArgTwo.lessThan(shortArgOne).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)shortArgTwo.greaterThan(shortArgOne).getValue());
    Assert.assertFalse((boolean)shortArgOne.greaterThan(shortArgTwo).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)shortArgTwo.equals(shortArgTwo).getValue());
    Assert.assertFalse((boolean)shortArgTwo.equals(shortArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((0 - (short)shortArgTwo.getValue()) ==
        (short)shortArgTwo.negate().getValue());
  }

}