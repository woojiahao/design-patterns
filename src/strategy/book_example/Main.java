package strategy.book_example;

public class Main {
  public static void main(String[] args) {
    AbstractDuck duck = new MallardDuck();
    duck.quack();
  }
}
