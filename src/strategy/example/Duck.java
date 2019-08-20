package strategy.example;

import strategy.example.fly.IFlyableBehavior;
import strategy.example.quack.IQuackableBehavior;

class Duck {
  // Variables kept private to ensure proper encapsulation
  private IFlyableBehavior flyBehavior;
  private IQuackableBehavior quackBehavior;
  private String name;

  // Sub classes just need to call the super() constructor to initialise all these attributes
  Duck(String name, IFlyableBehavior flyBehavior, IQuackableBehavior quackBehavior) {
    this.name = name;
    this.flyBehavior = flyBehavior;
    this.quackBehavior = quackBehavior;
  }

  // Regardless of what sub class is called, the quack() will always just delegate the work to whatever implementation
  // of IQuackableBehavior is given
  void quack() {
    quackBehavior.quack(name);
  }

  // Regardless of what sub class is called, the fly() will always just delegate the work to whatever implementation
  // of IFlyableBehavior is given
  void fly() {
    flyBehavior.fly(name);
  }

  // Allow the user to change the behavior during runtime if necessary
  void setFlyBehavior(IFlyableBehavior flyBehavior) {
    this.flyBehavior = flyBehavior;
  }

  // Allow the user to change the behavior during runtime if necessary
  void setQuackBehavior(IQuackableBehavior quackBehavior) {
    this.quackBehavior = quackBehavior;
  }
}
