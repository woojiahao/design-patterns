package factory.example.pizzastore;

import factory.example.PizzaType;
import factory.example.pizza.ChicagoStyleCheesePizza;
import factory.example.pizza.Pizza;

public class ChicagoPizzaStore extends PizzaStore {
  @Override Pizza createPizza(PizzaType type) {
    Pizza pizza;

    switch (type) {
      case CHEESE:
        pizza = new ChicagoStyleCheesePizza();
      default:
        pizza = null;
    }
    return pizza;
  }
}
