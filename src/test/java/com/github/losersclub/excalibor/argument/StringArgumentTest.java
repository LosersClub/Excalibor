package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.primitives.IntArgument;
import com.github.losersclub.excalibor.argument.primitives.StringArgument;

public class StringArgumentTest {
  private StringArgument stringArgOne = new StringArgument();
  private StringArgument stringArgTwo = new StringArgument("Hello");
  private StringArgument stringArgThree = new StringArgument("World!");

  @Test
  public void testParse() {
    Assert.assertNull(stringArgOne.parse("\"abc"));
    Assert.assertNull(stringArgOne.parse("a"));
    Assert.assertNull(stringArgOne.parse("a\""));
    Assert.assertNull(stringArgOne.parse("123"));
    Assert.assertNull(stringArgOne.parse(""));
    Assert.assertNull(stringArgOne.parse("'abc'"));
    Argument stringArgParsed =
        stringArgOne.parse("\"The inner workings of my mind are an enigma.\"");
    Assert.assertTrue(stringArgParsed instanceof StringArgument
        && ((String)stringArgParsed.getValue()).equals(
            "The inner workings of my mind are an enigma."));
    stringArgParsed = stringArgOne.parse("\"\\\"abc\\\"\"");
    Assert.assertTrue(stringArgParsed instanceof StringArgument
        && ((String)stringArgParsed.getValue()).equals("\"abc\""));
    stringArgParsed = stringArgOne.parse("\"\\\"\\\"\"");
    Assert.assertTrue(stringArgParsed instanceof StringArgument
        && ((String)stringArgParsed.getValue()).equals("\"\""));
    stringArgParsed = stringArgOne.parse("\"\\t\\n\\b\\r\\f\\\'\\\"\\\\\"");
    Assert.assertTrue(stringArgParsed instanceof StringArgument
        && ((String)stringArgParsed.getValue()).equals("\t\n\b\r\f\'\"\\"));
  }
  
  @Test
  public void testBuild() {
    Argument built = stringArgOne.build("Test");
    Assert.assertTrue(built instanceof StringArgument && ((String)built.getValue()).equals("Test"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBadEscape() {
    stringArgOne.parse("\"\\a\"");
  }
  
  @Test
  public void toStringTest() {
    Assert.assertTrue(stringArgTwo.toString().equals("\"Hello\""));
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)stringArgTwo.lessThan(stringArgThree).getValue());
    Assert.assertTrue((boolean)stringArgOne.lessThan(stringArgThree).getValue());
    Assert.assertFalse((boolean)stringArgThree.lessThan(stringArgTwo).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)stringArgThree.greaterThan(stringArgTwo).getValue());
    Assert.assertTrue((boolean)stringArgTwo.greaterThan(stringArgOne).getValue());
    Assert.assertFalse((boolean)stringArgTwo.greaterThan(stringArgThree).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)stringArgThree.equals(stringArgThree).getValue());
    Assert.assertFalse((boolean)stringArgThree.equals(stringArgOne).getValue());
  }

  @Test
  public void testConcat() {
    Assert.assertTrue(
        stringArgTwo.concat(stringArgThree).getValue().equals("HelloWorld!"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRHS() {
    IntArgument intArg = new IntArgument();
    stringArgOne.concat(intArg);
  }

}

