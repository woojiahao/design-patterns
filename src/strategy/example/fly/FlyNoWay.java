package strategy.example.fly;

public class FlyNoWay implements IFlyableBehavior {
  @Override public void fly(String name) {
    System.out.println(name + " cannot fly! :(");
  }
}
