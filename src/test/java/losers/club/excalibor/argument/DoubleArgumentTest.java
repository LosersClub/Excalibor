package losers.club.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;
import losers.club.excalibor.argument.primitives.DoubleArgument;
import losers.club.excalibor.argument.primitives.IntArgument;
import losers.club.excalibor.argument.primitives.StringArgument;


public class DoubleArgumentTest {
  private DoubleArgument doubleArgOne = new DoubleArgument();
  private DoubleArgument doubleArgTwo = new DoubleArgument(1.5);
  private DoubleArgument doubleArgThree = new DoubleArgument(2);

  @Test
  public void testParse() {
    Assert.assertNull(doubleArgOne.parse("'1.02'"));
    Assert.assertNull(doubleArgOne.parse("a12"));
    Assert.assertNull(doubleArgOne.parse("1234"));
    Assert.assertNull(doubleArgOne.parse("1.23.4"));
    Assert.assertNull(doubleArgOne.parse(""));
    Argument doubleArgParsed = doubleArgOne.parse("1.23");
    Assert.assertTrue(doubleArgParsed instanceof DoubleArgument
        && (double)doubleArgParsed.getValue() == 1.23);
    doubleArgParsed = doubleArgOne.parse(".456");
    Assert.assertTrue(doubleArgParsed instanceof DoubleArgument
        && (double)doubleArgParsed.getValue() == 0.456);
    doubleArgParsed = doubleArgOne.parse("81.46");
    Assert.assertTrue(doubleArgParsed instanceof DoubleArgument
        && (double)doubleArgParsed.getValue() == 81.46);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidRHS() {
    StringArgument stringArg = new StringArgument();
    doubleArgOne.add(stringArg);
  }

  @Test
  public void testNonDoubleRHS() {
   IntArgument intArg = new IntArgument(4);
   Argument doubleArgParsed = doubleArgTwo.add(intArg);
   Assert.assertTrue(doubleArgParsed instanceof DoubleArgument
       && (double)doubleArgParsed.getValue() == 5.5);
  }

  @Test
  public void testAdd() {
    Assert.assertTrue(1.5 == (double)doubleArgOne.add(doubleArgTwo).getValue());
  }

  @Test
  public void testSubtract() {
    Assert.assertTrue(-1.5 == (double)doubleArgOne.subtract(doubleArgTwo).getValue());
  }

  @Test
  public void testMultiply() {
    Assert.assertTrue(3.0 == (double)doubleArgThree.multiply(doubleArgTwo).getValue());
  }

  @Test
  public void testDivide() {
    Assert.assertTrue(0.75 == (double)doubleArgTwo.divide(doubleArgThree).getValue());
  }

  @Test
  public void testModulo() {
    Assert.assertTrue(0.0 == (double)doubleArgOne.modulo(doubleArgTwo).getValue());
    Assert.assertTrue(0.5 == (double)doubleArgThree.modulo(doubleArgTwo).getValue());
  }

  @Test
  public void testLessThan() {
    Assert.assertTrue((boolean)doubleArgOne.lessThan(doubleArgTwo).getValue());
    Assert.assertFalse((boolean)doubleArgTwo.lessThan(doubleArgOne).getValue());
  }

  @Test
  public void testGreaterThan() {
    Assert.assertTrue((boolean)doubleArgTwo.greaterThan(doubleArgOne).getValue());
    Assert.assertFalse((boolean)doubleArgOne.greaterThan(doubleArgTwo).getValue());
  }

  @Test
  public void testEquals() {
    Assert.assertTrue((boolean)doubleArgTwo.equals(doubleArgTwo).getValue());
    Assert.assertFalse((boolean)doubleArgTwo.equals(doubleArgOne).getValue());
  }

  @Test
  public void testNegate() {
    Assert.assertTrue((0 - (double)doubleArgTwo.getValue()) ==
        (double)doubleArgTwo.negate().getValue());
  }

}