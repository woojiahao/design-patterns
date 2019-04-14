package singleton.example;

public class EagerChocolateBoiler {
  private boolean empty = true;
  private boolean boiled = false;

  private static EagerChocolateBoiler instance = new EagerChocolateBoiler();

  private EagerChocolateBoiler() {}

  public static EagerChocolateBoiler getInstance() {
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
