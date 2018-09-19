package com.github.losersclub.excalibor.argument;

import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.argument.primitives.GenericArgument;

public class GenericArgumentTest {
  
  @Test
  public void equals() {
    GenericArgument gen1 = new GenericArgument(new Object());
    GenericArgument gen2 = new GenericArgument(new Object());
    
    Assert.assertTrue((boolean)gen1.equals(gen1).getValue());
    Assert.assertFalse((boolean)gen1.equals(gen2).getValue());
  }
  
  @Test
  public void parse() {
    Assert.assertTrue(new GenericArgument(new Object()).parse("anything") == null);
  }
  
  @Test
  public void build() {
    Assert.assertTrue(new GenericArgument(new Object()).build(new Object()) == null);
  }
  
  @Test
  public void convert() {
    GenericArgument arg = new GenericArgument(null);
    Assert.assertTrue(arg.convert(new GenericArgument(new Object())) == null);
  }
}
