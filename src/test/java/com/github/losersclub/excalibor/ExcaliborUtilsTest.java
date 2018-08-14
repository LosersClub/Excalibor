package com.github.losersclub.excalibor;

import org.junit.Assert;
import org.junit.Test;

public class ExcaliborUtilsTest {
  @Test
  public void testIsValidVarName() {
    String[] badStrings = {null, "", ".abc", "abc.", ".abc.", "a.bc", "a bc", "a!bc",
        "a-bc", "__", "123", "12_", "12a", "abc(", "abc()"};
    String[] goodStrings = {"abc", "a_bc", "_abc_", "a12", "a_12", "_ab", "ab_", "_12", "_a12"};
    for (String s : badStrings) {
      Assert.assertFalse(ExcaliborUtils.isValidVarName(s));
    }
    for (String s : goodStrings) {
      Assert.assertTrue(ExcaliborUtils.isValidVarName(s));
    }
  }
  
  @Test
  public void isSymbol() {
    char[] symbols = { '+', '-', ',', '/', '\\', '*', '^', '%', '#', '@', '!', ';', '?', '~', '$'};
    char[] notSymbols = { 'a', 'c', ' ', '\n', '\t', '_', '"', '\'', '.', ']', '[', ')', '(', '2'};
    for (char c : symbols) {
      Assert.assertTrue(ExcaliborUtils.isSymbol(c));
    }
    for (char c : notSymbols) {
      Assert.assertFalse(ExcaliborUtils.isSymbol(c));
    }
  }
  
  @Test(expected = InvalidExpressionException.class)
  public void reachEndOfContainerUnclosed() {
    ExcaliborUtils.reachEndOfContainer("\"test", 0, '"', new StringBuilder());
  }
  
  @Test
  public void reachEndOfContainer() {
    StringBuilder sb = new StringBuilder();
    int out = ExcaliborUtils.reachEndOfContainer("\"basic\"", 0, '"', sb);
    Assert.assertTrue(out == 6);
    out = ExcaliborUtils.reachEndOfContainer("([\"diff\"])", 0, ')', sb);
    Assert.assertTrue(out == 9);
    out = ExcaliborUtils.reachEndOfContainer("\"Test()\"", 5, ')', sb);
    Assert.assertTrue(out == 6);
    out = ExcaliborUtils.reachEndOfContainer("[mult[iple]ones]", 0, ']', sb);
    Assert.assertTrue(out == 15);
    out = ExcaliborUtils.reachEndOfContainer("\"Excap\\\"ed\"", 0, '"', sb);
    Assert.assertTrue(out == 10);
  }
}
