package factory.example.pizzastore;

import factory.example.pizza.NYStyleCheesePizza;
import factory.example.pizza.Pizza;

public class NYPizzaStore extends PizzaStore {
  @Override Pizza createPizza(String type) {
    Pizza pizza;

    if ("cheese".equals(type)) {
      pizza = new NYStyleCheesePizza();
    } else {
      pizza = null;
    }
    return pizza;
  }
}
