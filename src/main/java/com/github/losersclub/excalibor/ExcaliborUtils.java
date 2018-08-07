package com.github.losersclub.excalibor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class ExcaliborUtils {
  private ExcaliborUtils() {}
  
  private static List<Character> acceptableSymbols =
      Arrays.asList('\'', '"', '[', ']', '(', ')', '.', '_');
  
  public static Collection<Character> acceptableSymbols() {
    return Collections.unmodifiableCollection(acceptableSymbols);
  }
  
  public static int reachEndOfContainerNoThrow(String expression, int startIndex,
      char endChar, StringBuilder buffer) {
    try {
      return reachEndOfContainer(expression, startIndex, endChar, buffer);
    } catch (IllegalArgumentException e) {
      return -1;
    }
  }
  
  public static int reachEndOfContainer(String expression, int startIndex,
      char endChar, StringBuilder buffer) {
    int containerCounter = 1;
    char previousChar = endChar;
    for (int index = startIndex + 1; index < expression.length(); index++) {
      if (expression.charAt(index) == endChar && previousChar != '\\') {
        containerCounter--;
        if (containerCounter == 0) {
          return index;
        }
      }
      else if (expression.charAt(index) == expression.charAt(startIndex) && previousChar != '\\') {
        containerCounter++;
      }
      previousChar = expression.charAt(index);
      buffer.append(previousChar);
    }
    throw new IllegalArgumentException("Unclosed container. Expected an " + endChar);
  }
  
  public static boolean isSymbol(char c) {
    return !Character.isWhitespace(c) && !Character.isDigit(c) && !Character.isLetter(c)
        && !acceptableSymbols().contains(c);
  }
  
  public static boolean isValidVarName(String str) {
    if (str == null) {
      return false;
    }
    str = str.trim();
    boolean underscoreEncountered = false;
    boolean numberEncountered = false;
    boolean letterEncountered = false;
    if (str.isEmpty() || Character.isDigit(str.charAt(0))) {
      return false;
    }
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c == '.' || (!Character.isDigit(c) && !Character.isLetter(c) && c != '_')) {
        return false;
      }
      letterEncountered |= Character.isLetter(c);
      numberEncountered |= Character.isDigit(c);
      underscoreEncountered |= str.charAt(i) == '_';
    }
    if (underscoreEncountered && !(numberEncountered || letterEncountered)) {
      return false;
    }
    return true;
  }
}
