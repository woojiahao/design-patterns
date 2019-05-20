package adapter.example;

public class TurkeyAdapter implements IDuck {
  private final ITurkey turkey;

  public TurkeyAdapter(final ITurkey turkey) {
    this.turkey = turkey;
  }

  @Override public void fly() {
    System.out.println("Turkeys cannot fly!");
  }

  @Override public void quack() {
    turkey.gobble();
  }
}
