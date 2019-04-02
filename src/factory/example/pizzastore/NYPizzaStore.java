package factory.example.pizzastore;

import factory.example.PizzaType;
import factory.example.pizza.NYStyleCheesePizza;
import factory.example.pizza.Pizza;

public class NYPizzaStore extends PizzaStore {
  @Override Pizza createPizza(PizzaType type) {
    Pizza pizza;

    switch (type) {
      case CHEESE:
        pizza = new NYStyleCheesePizza();
      default:
        pizza = null;
    }
    return pizza;
  }
}
