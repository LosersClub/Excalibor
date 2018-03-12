package losers.club.excalibor.argument;

@FunctionalInterface
public interface Method<T extends Argument> {
  T apply(T lhs, T rhs);
}
