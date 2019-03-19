package strategy.example;

import strategy.example.fly.FlyNoWay;
import strategy.example.quack.Squeak;

class RubberDuck extends Duck {
  RubberDuck() {
    super("Rubber", new FlyNoWay(), new Squeak());
  }
}
