package com.github.losersclub.excalibor;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

import com.github.losersclub.excalibor.Excalibor;
import com.github.losersclub.excalibor.ExpressionCompiler;

public class ExcaliborTest {

  @Test
  public void testSetGetDefaultCompiler() {
    ExpressionCompiler expComp = new ExpressionCompiler();
    Assert.assertFalse(Excalibor.defaultCompiler() == expComp);
    Excalibor.setDefaultCompiler(expComp);
    Assert.assertTrue(Excalibor.defaultCompiler() == expComp);
    Excalibor.setDefaultCompiler(null);
    Assert.assertTrue(Excalibor.defaultCompiler() == expComp);
  }

  @Test
  public void testEvaluate() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("x", 5);

    ExpressionCompiler expComp = new ExpressionCompiler();
    String s = (String)Excalibor.evaluate("\"Hello\" + \" World!\"", expComp);
    Assert.assertTrue(s.equals("Hello World!"));

    int i = (int)Excalibor.evaluate("1 + 'c'");
    Assert.assertTrue(i == 100);

    double d = Excalibor.evaluate("1 / 2.0", Double.class);
    Assert.assertTrue(d == 0.5);

    i = (int)Excalibor.evaluate("x + 20", map);
    Assert.assertTrue(i == 25);

    i = Excalibor.evaluate("x + 6", map, Integer.class);
    Assert.assertTrue(i == 11);
  }

  @Test
  public void testCompile() {
    ExpressionCompiler expComp = new ExpressionCompiler();
    Assert.assertTrue(Excalibor.compile("(3 + 4) * 7", expComp).toString().equals("49"));

    Assert.assertTrue(Excalibor.compile("(3 + x) * 7").toString().equals("(3 + x) * 7"));
  }
  
  @Test
  public void nullTest() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("x", null);
    Assert.assertTrue(Excalibor.evaluate("x == null", map, Boolean.class));
    Assert.assertFalse(Excalibor.evaluate("x != null", map, Boolean.class));
    Assert.assertFalse(Excalibor.evaluate("null != x", map, Boolean.class));
    Assert.assertTrue(Excalibor.evaluate("null == x", map, Boolean.class));
  }
  
  @Test
  public void genericObjects() {
    Map<String, Object> map = new HashMap<String, Object>();
    Object obj1 = new Object();
    Object obj2 = new Object();
    map.put("x", obj1);    
    Assert.assertTrue(Excalibor.evaluate("x == x", map, Boolean.class));
    Assert.assertFalse(Excalibor.evaluate("x != x", map, Boolean.class));
    map.put("y", obj2);
    Assert.assertTrue(Excalibor.evaluate("x != y", map, Boolean.class));
    Assert.assertFalse(Excalibor.evaluate("x == y", map, Boolean.class));
  }
}