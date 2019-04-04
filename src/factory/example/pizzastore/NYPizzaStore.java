package factory.example.pizzastore;

import factory.example.PizzaType;
import factory.example.ingredients.IngredientFactory;
import factory.example.ingredients.NYIngredients;
import factory.example.pizza.CheesePizza;
import factory.example.pizza.Pizza;

public class NYPizzaStore extends PizzaStore {
  @Override Pizza createPizza(PizzaType type) {
    Pizza pizza;
    IngredientFactory ingredients = new NYIngredients();

    if (type == PizzaType.CHEESE) {
      pizza = new CheesePizza(ingredients);
      pizza.setName("New York Style Veggie Pizza");
    } else {
      pizza = null;
    }
    
    return pizza;
  }
}
