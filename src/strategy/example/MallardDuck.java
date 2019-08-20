package strategy.example;

import strategy.example.fly.FlyWithWings;
import strategy.example.quack.NormalQuack;

class MallardDuck extends Duck {
  MallardDuck() {
    super("Mallard", new FlyWithWings(), new NormalQuack());
  }
}
