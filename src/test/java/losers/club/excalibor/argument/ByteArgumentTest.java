package losers.club.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;
import losers.club.excalibor.argument.primitives.ByteArgument;
import losers.club.excalibor.argument.primitives.IntArgument;
import losers.club.excalibor.argument.primitives.StringArgument;

public class ByteArgumentTest {
  private ByteArgument byteArgOne = new ByteArgument();
  private ByteArgument byteArgTwo = new ByteArgument((byte)1);

  @Test
  public void testParse() {
    Assert.assertNull(byteArgOne.parse("ab"));
    Assert.assertNull(byteArgOne.parse("123"));
    Assert.assertNull(byteArgOne.parse("123456789101112b"));
    Assert.assertNull(byteArgOne.parse(""));
    Argument byteArgParsed = byteArgOne.parse("123b");
    Assert.assertTrue(byteArgParsed instanceof ByteArgument
        && (byte)byteArgParsed.getValue() == (byte)123);
    byteArgParsed = byteArgOne.parse("123B");
    Assert.assertTrue(byteArgParsed instanceof ByteArgument
        && (byte)byteArgParsed.getValue() == (byte)123);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    byteArgOne.add(stringArg);
  }

  @Test
  public void testNonByteRHS() {
   IntArgument intArg = new IntArgument(4);
   Argument byteArgParsed = byteArgOne.add(intArg);
   Assert.assertTrue(byteArgParsed instanceof ByteArgument
       && (byte)byteArgParsed.getValue() == (byte)4);
  }

  @Test
  public void testAdd() {
    Assert.assertEquals((byte)1, (byte)byteArgOne.add(byteArgTwo).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertEquals((byte)-1, (byte)byteArgOne.subtract(byteArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertEquals((byte)0, (byte)byteArgOne.multiply(byteArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertEquals((byte)0, (byte)byteArgOne.divide(byteArgTwo).getValue());
  }

  @Test
  public void testModulo() {
    ByteArgument byteArgThree = new ByteArgument((byte)2);
    ByteArgument byteArgFour = new ByteArgument((byte)3);
    Assert.assertEquals((byte)0, (byte)byteArgOne.modulo(byteArgTwo).getValue());
    Assert.assertEquals((byte)1, (byte)byteArgFour.modulo(byteArgThree).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)byteArgOne.lessThan(byteArgTwo).getValue());
    Assert.assertFalse((boolean)byteArgTwo.lessThan(byteArgOne).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)byteArgTwo.greaterThan(byteArgOne).getValue());
    Assert.assertFalse((boolean)byteArgOne.greaterThan(byteArgTwo).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)byteArgTwo.equals(byteArgTwo).getValue());
    Assert.assertFalse((boolean)byteArgTwo.equals(byteArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((0 - (byte)byteArgTwo.getValue()) == (byte)byteArgTwo.negate().getValue());
  }

}