package factory.example.pizza;

import factory.example.ingredients.Pepperoni;
import factory.example.ingredients.cheese.Cheese;
import factory.example.ingredients.clam.Clam;
import factory.example.ingredients.dough.Dough;
import factory.example.ingredients.sauce.Sauce;
import factory.example.ingredients.veggie.Veggie;

import java.util.List;

public abstract class Pizza {
  private String name;

  protected Dough dough;
  protected Cheese cheese;
  protected Clam clam;
  protected Sauce sauce;
  protected Veggie[] veggies;
  protected Pepperoni pepperoni;

  private List<String> toppings;

  abstract public void prepare();

  public void bake() {
    System.out.println("Bake for 30 minutes at 350");
  }

  public void cut() {
    System.out.println("Cutting the pizza in diagonal slices");
  }

  public void box() {
    System.out.println("Place pizza in official PizzaStore box");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
