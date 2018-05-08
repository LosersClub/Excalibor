package losers.club.excalibor.argument;

@FunctionalInterface
public interface Method<T extends Argument> {
  T run(Argument lhs, Argument rhs);
}
