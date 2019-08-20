package factory.example.pizza;

import factory.example.ingredients.IngredientFactory;

public class HawaiianPizza extends Pizza {
  private IngredientFactory ingredientFactory;

  public HawaiianPizza(IngredientFactory ingredientFactory) {
    this.ingredientFactory = ingredientFactory;
  }

  @Override public void prepare() {
    System.out.println("Preparing...");
    dough = ingredientFactory.createDough();
    sauce = ingredientFactory.createSauce();
    veggies = ingredientFactory.createVeggies();
    pepperoni = ingredientFactory.createPepperoni();
  }
}
