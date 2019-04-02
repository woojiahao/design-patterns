package factory.example;

import factory.example.pizzastore.ChicagoPizzaStore;
import factory.example.pizzastore.NYPizzaStore;
import factory.example.pizzastore.PizzaStore;

public class Main {
  public static void main(String[] args) {
    PizzaStore nyStore = new NYPizzaStore();
    nyStore.orderPizza("cheese");

    System.out.println();

    PizzaStore chicagoStore = new ChicagoPizzaStore();
    chicagoStore.orderPizza("cheese");
  }
}
