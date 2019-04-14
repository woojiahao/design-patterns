package singleton.example;

public class Main {
  public static void main(String[] args) {
    SimpleChocolateBoiler simpleChocolateBoiler = SimpleChocolateBoiler.getInstance();
    simpleChocolateBoiler.boil();

    Thread t1 = new Thread(() -> {
      SynchronizedChocolateBoiler synchronizedChocolateBoiler = SynchronizedChocolateBoiler.getInstance();
      synchronizedChocolateBoiler.boil();
    });
    Thread t2 = new Thread(() -> {
      SynchronizedChocolateBoiler synchronizedChocolateBoiler = SynchronizedChocolateBoiler.getInstance();
      synchronizedChocolateBoiler.drain();
    });
    t1.start();
    t2.start();

    EagerChocolateBoiler eagerChocolateBoiler = EagerChocolateBoiler.getInstance();
    eagerChocolateBoiler.fill();

    Thread t3 = new Thread(() -> {
      DoubleCheckedChocolateBoiler doubleCheckedChocolateBoiler = DoubleCheckedChocolateBoiler.getInstance();
      doubleCheckedChocolateBoiler.boil();
    });
    Thread t4 = new Thread(() -> {
      DoubleCheckedChocolateBoiler doubleCheckedChocolateBoiler = DoubleCheckedChocolateBoiler.getInstance();
      doubleCheckedChocolateBoiler.drain();
    });
    t3.start();
    t4.start();
  }
}
