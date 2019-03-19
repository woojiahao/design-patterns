package strategy.example.fly;

public class RocketPoweredFly implements IFlyableBehavior {
  @Override public void fly(String name) {
    System.out.println(name + " is blasting off!");
  }
}
