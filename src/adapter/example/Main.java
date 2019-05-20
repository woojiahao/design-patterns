package adapter.example;

public class Main {
  public static void main(String[] args) {
    final IDuck mallardDuck = new MallardDuck();
    mallardDuck.quack();

    final ITurkey thanksgivingTurkey = new ThanksgivingTurkey();
    final IDuck turkeyToDuck = new TurkeyAdapter(thanksgivingTurkey);
    turkeyToDuck.quack();
    turkeyToDuck.fly();
  }
}
