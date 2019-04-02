package factory.example.pizza;

import java.util.Arrays;
import java.util.List;

public abstract class Pizza {
  private String name;
  private String dough;
  private String sauce;
  private List<String> toppings;

  public Pizza(String name, String dough, String sauce, String... toppings) {
    this.name = name;
    this.dough = dough;
    this.sauce = sauce;
    this.toppings = Arrays.asList(toppings);
  }

  public void prepare() {
    System.out.println("Preparing " + name);
    System.out.println("Tossing dough...");
    System.out.println("Adding sauce...");
    System.out.println("Adding toppings:");
    toppings.forEach(topping -> System.out.println("\t" + topping));
  }

  public void bake() {
    System.out.println("Bake for 30 minutes at 350");
  }

  public void cut() {
    System.out.println("Cutting the pizza in diagonal slices");
  }

  public void box() {
    System.out.println("Place pizza in official PizzaStore box");
  }
}
