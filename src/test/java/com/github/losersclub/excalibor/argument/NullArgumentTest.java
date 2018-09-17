package com.github.losersclub.excalibor.argument;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.github.losersclub.excalibor.argument.primitives.BooleanArgument;
import com.github.losersclub.excalibor.argument.primitives.NullArgument;

public class NullArgumentTest {
  private NullArgument arg = new NullArgument();
  
  @Test
  public void parseNull() {
    assertThat(arg.parse("null"), is(arg));
  }
  
  @Test
  public void parseNotNull() {
    assertThat(arg.parse("4"), nullValue());
  }
  
  @Test
  public void build() {
    assertThat(arg.build(null), is(arg));
    assertThat(arg.build(4), nullValue());
  }
  
  @Test
  public void convert() {
    assertThat(arg.convert(arg), is(arg));
    assertThat(arg.convert(new BooleanArgument()), nullValue());
  }
  
  @Test
  public void toStringTest() {
    assertThat(arg.toString(), is("null"));
  }
  
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void equalsOverride() {
    assertThat(arg.equals((Object)arg), is(true));
    assertThat(arg.equals((Object)null), is(true));
    assertThat(arg.equals(4), is(false));
    assertThat(arg.equals((Object)new NullArgument()), is(true));
    assertThat(arg.equals((Object)new BooleanArgument()), is(false));
  }
  
  @Test
  public void equalsArg() {
    assertThat(arg.equals(new BooleanArgument(false)).getValue(), is(false));
    assertThat(arg.equals(arg).getValue(), is(true));
  }
}
