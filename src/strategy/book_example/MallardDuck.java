package strategy.book_example;

class MallardDuck extends AbstractDuck {
  MallardDuck() {
    name = "Mallard";
  }

  @Override void quack() {
    System.out.println(name + " says quack!");
  }

  @Override void fly() {
    System.out.println(name + " can fly!");
  }
}
