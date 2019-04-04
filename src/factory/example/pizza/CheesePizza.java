package factory.example.pizza;

import factory.example.ingredients.IngredientFactory;

public class CheesePizza extends Pizza {
  private IngredientFactory ingredientFactory;

  public CheesePizza(IngredientFactory ingredientFactory) {
    this.ingredientFactory = ingredientFactory;
  }

  @Override public void prepare() {
    System.out.println("Preparing...");
    dough = ingredientFactory.createDough();
    cheese = ingredientFactory.createCheese();
    sauce = ingredientFactory.createSauce();
    System.out.println("Type of dough: " + dough.getName());
  }
}
