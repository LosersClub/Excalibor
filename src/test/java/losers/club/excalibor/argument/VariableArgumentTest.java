package losers.club.excalibor.argument;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import losers.club.excalibor.ExpressionCompiler;

@RunWith(MockitoJUnitRunner.class)
public class VariableArgumentTest {
  @Mock ExpressionCompiler compiler;
  private VariableArgument vArg;
  
  @Before
  public void before() {
    this.vArg = new VariableArgument(compiler);
  }
  
  @Test
  public void isEvaluable() {
    Assert.assertFalse(this.vArg.isEvaluable());
  }
  
  @Test
  public void setEvaluable() {
    this.vArg.setEvaluable(true);
    Assert.assertTrue(this.vArg.isEvaluable());
  }
  
  @Test
  public void setValue() {
    Object newObj = new Object();
    this.vArg.setValue(null);
    Assert.assertFalse(this.vArg.isEvaluable());
    this.vArg.setValue(newObj);
    Assert.assertTrue(this.vArg.isEvaluable());
    this.vArg.setValue(null);
    Assert.assertFalse(this.vArg.isEvaluable());
  }
  
  @Test
  public void getValue() {
    Object newObj = new Object();
    this.vArg.setValue(newObj);
    Assert.assertTrue(newObj == this.vArg.getValue());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void parse() {
    this.vArg.parse("test");
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void build() {
    this.vArg.build(null);
  }
  
  @Test
  public void convertNull() {
    Assert.assertTrue(this.vArg.convert() == null);
  }
  
  @Test
  public void noValidParser() {
    Mockito.when(compiler.getParsers()).thenReturn(new ArrayList<>());
    this.vArg.setValue(new Object());
    Assert.assertTrue(this.vArg.convert() == null);
  }
  
  @Test
  public void validParser() {
    Argument parser1 = Mockito.mock(Argument.class);
    Argument parser2 = Mockito.mock(Argument.class);
    Argument out = Mockito.mock(Argument.class);
    Mockito.when(parser1.convert(vArg)).thenReturn(null);
    Mockito.when(parser2.convert(vArg)).thenReturn(out);
    Mockito.when(compiler.getParsers()).thenReturn(Arrays.asList(parser1, parser2));
    this.vArg.setValue(new Object());
    Assert.assertTrue(this.vArg.convert() == out);
    Mockito.verify(parser1, Mockito.times(1)).convert(vArg);
    Mockito.verify(parser2, Mockito.times(1)).convert(vArg);
  }
}
