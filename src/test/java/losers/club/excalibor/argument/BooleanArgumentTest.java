package losers.club.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;
import losers.club.excalibor.argument.primitives.BooleanArgument;
import losers.club.excalibor.argument.primitives.IntArgument;

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
  public void testNot() throws Exception {
    Assert.assertTrue((boolean)boolArgFalse.not().getValue());
    Assert.assertFalse((boolean)boolArgTrue.not().getValue());
  }

  @Test(expected = IllegalArgumentException.class)
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