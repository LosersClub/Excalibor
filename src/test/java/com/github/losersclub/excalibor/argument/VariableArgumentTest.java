package com.github.losersclub.excalibor.argument;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.losersclub.excalibor.ExpressionCompiler;
import com.github.losersclub.excalibor.argument.Argument;
import com.github.losersclub.excalibor.argument.VariableArgument;
import com.github.losersclub.excalibor.argument.primitives.NullArgument;

@RunWith(MockitoJUnitRunner.class)
public class VariableArgumentTest {
  @Mock ExpressionCompiler compiler;
  private VariableArgument vArg;

  @Before
  public void before() {
    this.vArg = new VariableArgument(compiler, "x");
  }

  @Test
  public void isEvaluable() {
    Assert.assertFalse(this.vArg.isEvaluable());
  }
  
  @Test
  public void nullConvert() {
    Assert.assertTrue(this.vArg.convert(this.vArg) instanceof NullArgument);
  }

  @Test
  public void setValue() {
    Object newObj = new Object();
    this.vArg.setValue(null);
    Assert.assertTrue(this.vArg.isEvaluable());
    this.vArg.setValue(newObj);
    Assert.assertTrue(this.vArg.isEvaluable());
    this.vArg.setValue(null);
    Assert.assertTrue(this.vArg.isEvaluable());
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
    Assert.assertTrue(this.vArg.convert() == this.vArg);
  }

  @Test
  public void noValidParser() {
    Mockito.when(compiler.getArguments()).thenReturn(new ArrayList<>());
    this.vArg.setValue(new Object());
    Assert.assertTrue(this.vArg.convert() == this.vArg);
  }

  @Test
  public void validParser() {
    Argument parser1 = Mockito.mock(Argument.class);
    Argument parser2 = Mockito.mock(Argument.class);
    Argument out = Mockito.mock(Argument.class);
    Mockito.when(parser1.convert(vArg)).thenReturn(null);
    Mockito.when(parser2.convert(vArg)).thenReturn(out);
    Mockito.when(compiler.getArguments()).thenReturn(Arrays.asList(parser1, parser2));
    this.vArg.setValue(new Object());
    Assert.assertTrue(this.vArg.convert() == out);
    Mockito.verify(parser1, Mockito.times(1)).convert(vArg);
    Mockito.verify(parser2, Mockito.times(1)).convert(vArg);
  }
}
