package factory.example.pizzastore;

import factory.example.PizzaType;
import factory.example.ingredients.ChicagoIngredients;
import factory.example.ingredients.IngredientFactory;
import factory.example.pizza.CheesePizza;
import factory.example.pizza.Pizza;

public class ChicagoPizzaStore extends PizzaStore {
  @Override Pizza createPizza(PizzaType type) {
    Pizza pizza;
    IngredientFactory ingredientFactory = new ChicagoIngredients();

    if (type == PizzaType.CHEESE) {
      pizza = new CheesePizza(ingredientFactory);
      pizza.setName("Chicago Style Pizza");
    } else {
      pizza = null;
    }

    return pizza;
  }
}
