package factory.example.pizzastore;

import factory.example.pizza.ChicagoStyleCheesePizza;
import factory.example.pizza.Pizza;

public class ChicagoPizzaStore extends PizzaStore {
  @Override Pizza createPizza(String type) {
    Pizza pizza;

    if (type.equals("cheese")) {
      pizza = new ChicagoStyleCheesePizza();
    } else {
      pizza = null;
    }
    return pizza;
  }
}
