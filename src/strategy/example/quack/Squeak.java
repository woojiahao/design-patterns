package strategy.example.quack;

public class Squeak implements IQuackableBehavior {
  @Override public void quack(String name) {
    System.out.println(name + " can't quack, but it can sure squeak!");
  }
}
