package singleton.example;

public class DoubleCheckedChocolateBoiler {
  private boolean empty = true;
  private boolean boiled = false;

  private static DoubleCheckedChocolateBoiler instance;

  private DoubleCheckedChocolateBoiler() {}

  public static DoubleCheckedChocolateBoiler getInstance() {
    if (instance == null) {
      synchronized (DoubleCheckedChocolateBoiler.class) {
        if (instance == null) {
          instance = new DoubleCheckedChocolateBoiler();
        }
      }
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
