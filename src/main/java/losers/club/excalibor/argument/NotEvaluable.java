package losers.club.excalibor.argument;

public interface NotEvaluable {
  public boolean isEvaluable();
  
  public void setEvaluable(boolean value);
  
  public Argument convert();
}
