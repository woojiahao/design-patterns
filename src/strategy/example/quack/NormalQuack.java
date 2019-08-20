package strategy.example.quack;

public class NormalQuack implements IQuackableBehavior {
  @Override public void quack(String name) {
    System.out.println(name + " goes quack quack!");
  }
}
