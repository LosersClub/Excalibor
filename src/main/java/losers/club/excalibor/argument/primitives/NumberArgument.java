package losers.club.excalibor.argument.primitives;

import losers.club.excalibor.argument.Argument;

public abstract class NumberArgument implements ComparableArgument {
  
  public Argument add(Argument rhs) {
    return cast(rhs, this.add(getRhsValue("add", rhs)));
  }
  public Argument subtract(Argument rhs) {
    return cast(rhs, this.subtract(getRhsValue("subtract", rhs)));
  }
  public Argument multiply(Argument rhs) {
    return cast(rhs, this.multiply(getRhsValue("multiply", rhs)));
  }
  public Argument divide(Argument rhs) {
    return cast(rhs, this.divide(getRhsValue("divide", rhs)));
  }
  public Argument modulo(Argument rhs) {
    return cast(rhs, this.modulo(getRhsValue("modulo", rhs)));
  }
  public abstract Argument negate();
  
  public abstract int priority();
  public abstract double getMathTypeValue();
  abstract double add(double rhs);
  abstract double subtract(double rhs);
  abstract double multiply(double rhs);
  abstract double divide(double rhs);
  abstract double modulo(double rhs);
  
  
  double getRhsValue(String op, Argument rhs) {
    if (rhs instanceof NumberArgument) {
      return ((NumberArgument)(rhs)).getMathTypeValue();
    }
    throw new IllegalArgumentException(String.format(
        "Incompatible types for %s operation: %s is type %s, %s is type %s", op, this.getValue(),
        this.getValue().getClass().getName(), rhs.getValue().toString(),
        rhs.getValue().getClass().getName()));
  }
  
  private Argument cast(Argument rhs, double val) {
    return this.priority() > ((NumberArgument)rhs).priority() ? this.build(val) : rhs.build(val);
  }
}
