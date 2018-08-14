package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.InvalidExpressionException;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.primitives.CharArgument;
import com.github.losersclub.excalibor.argument.primitives.IntArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;

public class CharArgumentTest {
  private CharArgument charArgOne = new CharArgument();
  private CharArgument charArgTwo = new CharArgument((char)1);

  @Test
  public void testParse() {
    Assert.assertNull(charArgOne.parse("'u0000"));
    Assert.assertNull(charArgOne.parse("1"));
    Assert.assertNull(charArgOne.parse("1'"));
    Assert.assertNull(charArgOne.parse("'12345678'"));
    Assert.assertNull(charArgOne.parse("'\\12345678'"));
    Assert.assertNull(charArgOne.parse("'\\ufffg'"));
    Assert.assertNull(charArgOne.parse(""));
    Argument charArgParsed = charArgOne.parse("'a'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == 'a');
    charArgParsed = charArgOne.parse("'\\uffff'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\uffff');
    charArgParsed = charArgOne.parse("'\\u0123'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\u0123');
    charArgParsed = charArgOne.parse("'\\''");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\'');
    charArgParsed = charArgOne.parse("'\\t'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\t');
    charArgParsed = charArgOne.parse("'\\n'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\n');
    charArgParsed = charArgOne.parse("'\\b'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\b');
    charArgParsed = charArgOne.parse("'\\r'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\r');
    charArgParsed = charArgOne.parse("'\\f'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\f');
    charArgParsed = charArgOne.parse("'\\\"'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\"');
    charArgParsed = charArgOne.parse("'\\\\'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\\');
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void testInvalidEscape() {
    charArgOne.parse("'\\v'");
  }

  @Test
  public void testBuild() {
    Argument built = charArgOne.build((char)2);
    Assert.assertTrue(built instanceof CharArgument && (char)built.getValue() == (char)2);
  }
  
  @Test(expected = ClassCastException.class)
  public void testBuildCast() {
    charArgOne.build("test");
  }

  @Test (expected = InvalidExpressionException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    charArgOne.add(stringArg);
  }
  
  @Test
  public void toStringTest() {
    Assert.assertTrue(charArgOne.parse("'c'").toString().equals("'c'"));
  }

  @Test
  public void testNonCharRHS() {
   IntArgument intArg = new IntArgument(4);
   Argument charArgParsed = charArgTwo.add(intArg);
   Assert.assertTrue(charArgParsed instanceof IntArgument
       && (int)charArgParsed.getValue() == 5);
  }

  @Test
  public void testAdd() {
    Assert.assertEquals((char)1, (char)charArgOne.add(charArgTwo).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertEquals((char)-1, (char)charArgOne.subtract(charArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertEquals((char)0, (char)charArgOne.multiply(charArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertEquals((char)0, (char)charArgOne.divide(charArgTwo).getValue());
  }

  @Test
  public void testModulo() {
    CharArgument charArgThree = new CharArgument((char)2);
    CharArgument charArgFour = new CharArgument((char)3);
    Assert.assertEquals((char)0, (char)charArgOne.modulo(charArgTwo).getValue());
    Assert.assertEquals((char)1, (char)charArgFour.modulo(charArgThree).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)charArgOne.lessThan(charArgTwo).getValue());
    Assert.assertFalse((boolean)charArgTwo.lessThan(charArgOne).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)charArgTwo.greaterThan(charArgOne).getValue());
    Assert.assertFalse((boolean)charArgOne.greaterThan(charArgTwo).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)charArgTwo.equals(charArgTwo).getValue());
    Assert.assertFalse((boolean)charArgTwo.equals(charArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((char)(0 - (char)charArgTwo.getValue()) ==
        (char)charArgTwo.negate().getValue());
  }

}