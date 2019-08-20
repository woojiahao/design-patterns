package adapter.example;

public class MallardDuck implements IDuck {
  @Override public void fly() {
    System.out.println("Mallard ducks assemble and fly!");
  }

  @Override public void quack() {
    System.out.println("Quack quack!");
  }
}
