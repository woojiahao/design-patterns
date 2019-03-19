package strategy.example.fly;

public class FlyWithWings implements IFlyableBehavior {
  @Override public void fly(String name) {
    System.out.println(name + " is flapping its wings in the air!");
  }
}
