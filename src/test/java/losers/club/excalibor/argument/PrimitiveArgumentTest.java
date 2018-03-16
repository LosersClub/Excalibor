package losers.club.excalibor.argument;

import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;
import org.junit.Test;
import losers.club.excalibor.argument.primitives.*;

public class PrimitiveArgumentTest {

  private ByteArgument byteArg = new ByteArgument();
  private CharArgument charArg = new CharArgument();
  private DoubleArgument doubleArg = new DoubleArgument();
  private FloatArgument floatArg = new FloatArgument();
  private IntArgument intArg = new IntArgument();
  private LongArgument longArg = new LongArgument();
  private ShortArgument shortArg = new ShortArgument();

  private StringArgument stringArg = new StringArgument();
  private BooleanArgument boolArg = new BooleanArgument();

  @Test
  public void testGetValue() throws Exception {
    Assert.assertTrue(byteArg.getValue() instanceof Byte
        && (byte)byteArg.getValue() == (byte)0);
    Assert.assertTrue(charArg.getValue() instanceof Character
        && (char)charArg.getValue() == '\u0000');
    Assert.assertTrue(doubleArg.getValue() instanceof Double
        && (double)doubleArg.getValue() == 0.0);
    Assert.assertTrue(floatArg.getValue() instanceof Float
        && (float)floatArg.getValue() == 0.0f);
    Assert.assertTrue(intArg.getValue() instanceof Integer
        && (int)intArg.getValue() == 0);
    Assert.assertTrue(longArg.getValue() instanceof Long
        && (long)longArg.getValue() == 0L);
    Assert.assertTrue(shortArg.getValue() instanceof Short
        && (short)shortArg.getValue() == (short)0);
    Assert.assertTrue(stringArg.getValue() instanceof String
        && ((String)stringArg.getValue()).equals(""));
    Assert.assertFalse(boolArg.getValue() instanceof Boolean
        && (boolean)boolArg.getValue());
  }

  @Test
  public void testParseSuccess() throws Exception {
    Argument byteArgParsed = byteArg.parse("12b");
    Assert.assertTrue(byteArgParsed instanceof ByteArgument
        && (byte)byteArgParsed.getValue() == (byte)12);
    Argument charArgParsed = charArg.parse("'\u1000'");
    Assert.assertTrue(charArgParsed instanceof CharArgument
        && (char)charArgParsed.getValue() == '\u1000');
    Argument doubleArgParsed = doubleArg.parse("1.23");
    Assert.assertTrue(doubleArgParsed instanceof DoubleArgument
        && (double)doubleArgParsed.getValue() == 1.23);
    Argument floatArgParsed = floatArg.parse("1.23f");
    Assert.assertTrue(floatArgParsed instanceof FloatArgument
        && (float)floatArgParsed.getValue() == 1.23f);
    Argument intArgParsed = intArg.parse("23");
    Assert.assertTrue(intArgParsed instanceof IntArgument
        && (int)intArgParsed.getValue() == 23);
    Argument longArgParsed = longArg.parse("5L");
    Assert.assertTrue(longArgParsed instanceof LongArgument
        && (long)longArgParsed.getValue() == 5L);
    Argument shortArgParsed = shortArg.parse("3s");
    Assert.assertTrue(shortArgParsed instanceof ShortArgument
        && (short)shortArgParsed.getValue() == (short)3);
    Argument stringArgParsed = stringArg.parse("\"hello world\"");
    Assert.assertTrue(stringArgParsed instanceof StringArgument
        && ((String)stringArgParsed.getValue()).equals("hello world"));
    Argument boolArgParsed = boolArg.parse("true");
    Assert.assertTrue(boolArgParsed instanceof BooleanArgument
        && (boolean)boolArgParsed.getValue());
  }

  @Test
  public void testParseFailure() throws Exception {

  }
}
