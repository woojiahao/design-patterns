package singleton.example;

public class SimpleChocolateBoiler {
  private boolean empty = true;
  private boolean boiled = false;

  private static SimpleChocolateBoiler instance;

  private SimpleChocolateBoiler() {}

  public static SimpleChocolateBoiler getInstance() {
    if (instance == null) {
      instance = new SimpleChocolateBoiler();
    }

    return instance;
  }

  public void fill() {
    if (empty) {
      empty = false;
      boiled = false;
      // fill boiler with milk/chocolate mixture
    }
  }

  public void drain() {
    if (!empty && boiled) {
      empty = true;
    }
  }

  public void boil() {
    if (!empty && !boiled) {
      boiled = true;
    }
  }

  public boolean isEmpty() {
    return empty;
  }

  public boolean isBoiled() {
    return boiled;
  }
}
