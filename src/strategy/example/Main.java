package strategy.example;

import strategy.example.fly.RocketPoweredFly;

public class Main {
  public static void main(String[] args) {
    // Program to an interface, not implementation
    Duck duck = new MallardDuck();
    duck.fly(); // Should exhibit FlyWithWings behavior
    duck.setFlyBehavior(new RocketPoweredFly());
    duck.fly(); // Should exhibit RocketPoweredFly behavior

    duck = new RubberDuck();
    duck.quack(); // Should exhibit Squeak behavior
  }
}
