package losers.club.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;
import losers.club.excalibor.argument.primitives.DoubleArgument;
import losers.club.excalibor.argument.primitives.IntArgument;
import losers.club.excalibor.argument.primitives.StringArgument;


public class IntArgumentTest {
  private IntArgument intArgOne = new IntArgument();
  private IntArgument intArgTwo = new IntArgument(5);
  private IntArgument intArgThree = new IntArgument(2);

  @Test
  public void testGetMethodList() {
    Assert.assertNotNull(IntArgument.getMethodList());
  }

  @Test
  public void testParse() {
    Assert.assertNull(intArgOne.parse("'1.02'"));
    Assert.assertNull(intArgOne.parse("a12"));
    Assert.assertNull(intArgOne.parse("1234f"));
    Assert.assertNull(intArgOne.parse("1.23.4d"));
    Assert.assertNull(intArgOne.parse("123456789101112"));
    Assert.assertNull(intArgOne.parse(""));
    Argument intArgParsed = intArgOne.parse("123");
    Assert.assertTrue(intArgParsed instanceof IntArgument
        && (int)intArgParsed.getValue() == 123);
    intArgParsed = intArgOne.parse("456");
    Assert.assertTrue(intArgParsed instanceof IntArgument
        && (int)intArgParsed.getValue() == 456);
    intArgParsed = intArgOne.parse("1234567891");
    Assert.assertTrue(intArgParsed instanceof IntArgument
        && (int)intArgParsed.getValue() == 1234567891);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    intArgOne.add(stringArg);
  }

  @Test
  public void testNonIntRHS() {
   DoubleArgument doubleArg = new DoubleArgument(4.00);
   Argument intArgParsed = intArgTwo.add(doubleArg);
   Assert.assertTrue(intArgParsed instanceof IntArgument
       && (int)intArgParsed.getValue() == 9);
  }

  @Test
  public void testAdd() {
    Assert.assertTrue(7 == (int)intArgTwo.add(intArgThree).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertTrue(-3 == (int)intArgThree.subtract(intArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertTrue(10 == (int)intArgThree.multiply(intArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertTrue(2 == (int)intArgTwo.divide(intArgThree).getValue());
  }

  @Test
  public void testModulo() {
    Assert.assertTrue(2 == (int)intArgThree.modulo(intArgTwo).getValue());
    Assert.assertTrue(1 == (int)intArgTwo.modulo(intArgThree).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)intArgOne.lessThan(intArgTwo).getValue());
    Assert.assertFalse((boolean)intArgTwo.lessThan(intArgOne).getValue());
  }

  @Test
  public void testLessThanEqualTo() {
    Assert.assertTrue((boolean)intArgOne.lessThanEqualTo(intArgTwo).getValue());
    Assert.assertTrue((boolean)intArgTwo.lessThanEqualTo(intArgTwo).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)intArgTwo.greaterThan(intArgOne).getValue());
    Assert.assertFalse((boolean)intArgOne.greaterThan(intArgTwo).getValue());
  }

  @Test
  public void testGreaterThanEqualTo() {
    Assert.assertTrue((boolean)intArgTwo.greaterThanEqualTo(intArgOne).getValue());
    Assert.assertTrue((boolean)intArgOne.greaterThanEqualTo(intArgOne).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)intArgTwo.equals(intArgTwo).getValue());
    Assert.assertFalse((boolean)intArgTwo.equals(intArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((0 - (int)intArgTwo.getValue()) == (int)intArgTwo.negate().getValue());
  }

  @Test
  public void testNotEquals() {
    Assert.assertTrue((boolean)intArgTwo.notEquals(intArgOne).getValue());
    Assert.assertFalse((boolean)intArgTwo.notEquals(intArgTwo).getValue());
  }

}