package losers.club.excalibor.operator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import losers.club.excalibor.argument.Argument;
import losers.club.excalibor.argument.primitives.BooleanArgument;
import losers.club.excalibor.argument.primitives.IntArgument;
import losers.club.excalibor.argument.primitives.StringArgument;
import losers.club.excalibor.operator.primitives.AddOperator;
import losers.club.excalibor.operator.primitives.AndOperator;
import losers.club.excalibor.operator.primitives.DivideOperator;
import losers.club.excalibor.operator.primitives.EqualsOperator;
import losers.club.excalibor.operator.primitives.GreaterThanEqOperator;
import losers.club.excalibor.operator.primitives.GreaterThanOperator;
import losers.club.excalibor.operator.primitives.LessThanEqOperator;
import losers.club.excalibor.operator.primitives.LessThanOperator;
import losers.club.excalibor.operator.primitives.ModuloOperator;
import losers.club.excalibor.operator.primitives.MultiplyOperator;
import losers.club.excalibor.operator.primitives.NegateOperator;
import losers.club.excalibor.operator.primitives.NotEqualsOperator;
import losers.club.excalibor.operator.primitives.NotOperator;
import losers.club.excalibor.operator.primitives.OrOperator;
import losers.club.excalibor.operator.primitives.XOrOperator;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class OperatorsTest {

  // Logical Argument
  @Mock private static BooleanArgument boolArgOne;
  @Mock private static BooleanArgument boolArgTwo;
  // Number Argument
  @Mock private static IntArgument intArgOne;
  @Mock private static IntArgument intArgTwo;
  // Comparable non-number Argument
  @Mock private static StringArgument stringArgOne;
  @Mock private static StringArgument stringArgTwo;


  @Test
  public void testAddOperator() {
    AddOperator addOp = new AddOperator();
    Assert.assertTrue(addOp.getSymbol().equals("+"));
    addOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).add(intArgTwo);
    addOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).concat(stringArgTwo);
  }

  @Test
  public void testAddOperatorInvalid() {
    AddOperator addOp = new AddOperator();
    Assert.assertTrue(exceptionHelper(() -> addOp.evaluate(boolArgOne, intArgOne)));
  }

  @Test
  public void testAndOperator() {
    AndOperator andOp = new AndOperator();
    Assert.assertTrue(andOp.getSymbol().equals("&&"));
    andOp.evaluate(boolArgOne, boolArgTwo);
    verify(boolArgOne, times(1)).and(boolArgTwo);
  }

  @Test
  public void testAndOperatorInvalid() {
    AndOperator andOp = new AndOperator();
    Assert.assertTrue(exceptionHelper(() -> andOp.evaluate(intArgOne, boolArgOne)));
    Assert.assertTrue(exceptionHelper(() -> andOp.evaluate(stringArgOne, boolArgOne)));
  }

  @Test
  public void testDivideOperator() {
    DivideOperator divideOp = new DivideOperator();
    Assert.assertTrue(divideOp.getSymbol().equals("/"));
    divideOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).divide(intArgTwo);
  }

  @Test
  public void testDivideOperatorInvalid() {
    DivideOperator divideOp= new DivideOperator();
    Assert.assertTrue(exceptionHelper(() -> divideOp.evaluate(stringArgOne, intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> divideOp.evaluate(boolArgOne, intArgOne)));
  }

  @Test
  public void testEqualsOperator() {
    EqualsOperator equalsOp = new EqualsOperator();
    Assert.assertTrue(equalsOp.getSymbol().equals("=="));
    equalsOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).equals(intArgTwo);
    equalsOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).equals(stringArgTwo);
    equalsOp.evaluate(boolArgOne, boolArgTwo);
    verify(boolArgOne, times(1)).equals(boolArgTwo);
  }

  @Test
  public void testEqualsOperatorInvalid() {
    EqualsOperator equalsOp = new EqualsOperator();
    Argument arg = mock(Argument.class);
    Assert.assertTrue(exceptionHelper(() -> equalsOp.evaluate(arg, intArgOne)));
  }

  @Test
  public void testGreaterThanEqOperator() {
    GreaterThanEqOperator gteOp = new GreaterThanEqOperator();
    Assert.assertTrue(gteOp.getSymbol().equals(">="));
    gteOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).greaterThanEqualTo(intArgTwo);
    gteOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).greaterThanEqualTo(stringArgTwo);
  }

  @Test
  public void testGreaterThanEqOperatorInvalid() {
    GreaterThanEqOperator gteOp = new GreaterThanEqOperator();
    Assert.assertTrue(exceptionHelper(() -> gteOp.evaluate(boolArgOne, intArgTwo)));
  }

  @Test
  public void testGreaterThanOperator() {
    GreaterThanOperator gtOp = new GreaterThanOperator();
    Assert.assertTrue(gtOp.getSymbol().equals(">"));
    gtOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).greaterThan(intArgTwo);
    gtOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).greaterThan(stringArgTwo);
  }

  @Test
  public void testGreaterThanOperatorInvalid() {
    GreaterThanOperator gtOp = new GreaterThanOperator();
    Assert.assertTrue(exceptionHelper(() -> gtOp.evaluate(boolArgOne, stringArgOne)));
  }

  @Test
  public void testLessThanEqOperator() {
    LessThanEqOperator lteOp = new LessThanEqOperator();
    Assert.assertTrue(lteOp.getSymbol().equals("<="));
    lteOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).lessThanEqualTo(intArgTwo);
    lteOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).lessThanEqualTo(stringArgTwo);
  }

  @Test
  public void testLessThanEqOperatorInvalid() {
    LessThanEqOperator lteOp = new LessThanEqOperator();
    Assert.assertTrue(exceptionHelper(() -> lteOp.evaluate(boolArgOne, intArgOne)));
  }

  @Test
  public void testLessThanOperator() {
    LessThanOperator ltOp = new LessThanOperator();
    Assert.assertTrue(ltOp.getSymbol().equals("<"));
    ltOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).lessThan(intArgTwo);
    ltOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).lessThan(stringArgTwo);
  }

  @Test
  public void testLessThanOperatorInvalid() {
    LessThanOperator ltOp = new LessThanOperator();
    Assert.assertTrue(exceptionHelper(() -> ltOp.evaluate(boolArgOne, stringArgOne)));
  }

  @Test
  public void testModuloOperator() {
    ModuloOperator modOp = new ModuloOperator();
    Assert.assertTrue(modOp.getSymbol().equals("%"));
    modOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).modulo(intArgTwo);
  }

  @Test
  public void testModuloOperatorInvalid() {
    ModuloOperator modOp = new ModuloOperator();
    Assert.assertTrue(exceptionHelper(() -> modOp.evaluate(stringArgOne, intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> modOp.evaluate(boolArgOne, intArgOne)));
  }

  @Test
  public void testMultiplyOperator() {
    MultiplyOperator multOp = new MultiplyOperator();
    Assert.assertTrue(multOp.getSymbol().equals("*"));
    multOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).multiply(intArgTwo);
  }

  @Test
  public void testMultiplyOperatorInvalid() {
    MultiplyOperator multOp = new MultiplyOperator();
    Assert.assertTrue(exceptionHelper(() -> multOp.evaluate(stringArgOne, intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> multOp.evaluate(boolArgOne, intArgOne)));
  }

  @Test
  public void testNegateOperator() {
    NegateOperator negOp = new NegateOperator();
    Assert.assertTrue(negOp.getSymbol().equals("-"));
    negOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).subtract(intArgTwo);
    negOp.evaluate(intArgTwo);
    negOp.evaluate(null, intArgTwo);
    verify(intArgTwo, times(2)).negate();
  }

  @Test
  public void testNegateOperatorInvalid() {
    NegateOperator negOp = new NegateOperator();
    Assert.assertTrue(exceptionHelper(() -> negOp.evaluate(stringArgOne, intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> negOp.evaluate(stringArgOne)));
    Assert.assertTrue(exceptionHelper(() -> negOp.evaluate(null, stringArgOne)));
    Assert.assertTrue(exceptionHelper(() -> negOp.evaluate(boolArgOne, intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> negOp.evaluate(boolArgOne)));
    Assert.assertTrue(exceptionHelper(() -> negOp.evaluate(null, boolArgOne)));
  }

  @Test
  public void testNotEqualsOperator() {
    NotEqualsOperator neOp = new NotEqualsOperator();
    Assert.assertTrue(neOp.getSymbol().equals("!="));
    neOp.evaluate(intArgOne, intArgTwo);
    verify(intArgOne, times(1)).notEquals(intArgTwo);
    neOp.evaluate(stringArgOne, stringArgTwo);
    verify(stringArgOne, times(1)).notEquals(stringArgTwo);
    neOp.evaluate(boolArgOne, boolArgTwo);
    verify(boolArgOne, times(1)).notEquals(boolArgTwo);
  }

  @Test
  public void testNotEqualsOperatorInvalid() {
    NotEqualsOperator neOp = new NotEqualsOperator();
    Argument arg = mock(Argument.class);
    Assert.assertTrue(exceptionHelper(() -> neOp.evaluate(arg, intArgOne)));
  }

  @Test
  public void testNotOperator() {
    NotOperator notOp = new NotOperator();
    Assert.assertTrue(notOp.getSymbol().equals("!"));
    notOp.evaluate(boolArgTwo);
    notOp.evaluate(null, boolArgTwo);
    verify(boolArgTwo, times(2)).not();
  }

  @Test
  public void testNotOperatorInvalid() {
    NotOperator notOp = new NotOperator();
    Assert.assertTrue(exceptionHelper(() -> notOp.evaluate(intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> notOp.evaluate(null, intArgOne)));
    Assert.assertTrue(exceptionHelper(() -> notOp.evaluate(stringArgOne)));
    Assert.assertTrue(exceptionHelper(() -> notOp.evaluate(null, stringArgOne)));
    Assert.assertTrue(exceptionHelper(() -> notOp.evaluate(stringArgOne, intArgOne)));
  }

  @Test
  public void testOrOperator() {
    OrOperator orOp = new OrOperator();
    Assert.assertTrue(orOp.getSymbol().equals("||"));
    orOp.evaluate(boolArgOne, boolArgTwo);
    verify(boolArgOne, times(1)).or(boolArgTwo);
  }

  @Test
  public void testOrOperatorInvalid() {
    OrOperator orOp = new OrOperator();
    Assert.assertTrue(exceptionHelper(() -> orOp.evaluate(intArgOne, boolArgOne)));
    Assert.assertTrue(exceptionHelper(() -> orOp.evaluate(stringArgOne, boolArgOne)));
  }

  @Test
  public void testXOrOperator() {
    XOrOperator xorOp = new XOrOperator();
    Assert.assertTrue(xorOp.getSymbol().equals("^"));
    xorOp.evaluate(boolArgOne, boolArgTwo);
    verify(boolArgOne, times(1)).xor(boolArgTwo);
  }

  @Test
  public void testXOrOperatorInvalid() {
    XOrOperator xorOp = new XOrOperator();
    Assert.assertTrue(exceptionHelper(() -> xorOp.evaluate(intArgOne, boolArgOne)));
    Assert.assertTrue(exceptionHelper(() -> xorOp.evaluate(stringArgOne, boolArgOne)));
  }

  @Test
  public void testOperatorHashCode() {
    Assert.assertTrue(new NegateOperator().hashCode() == "-".hashCode());
  }

  private static boolean exceptionHelper(Runnable call) {
    try {
      call.run();
    } catch (IllegalArgumentException e) {
      return true;
    }
    return false;
  }
}