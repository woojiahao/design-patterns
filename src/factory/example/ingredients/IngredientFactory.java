package factory.example.ingredients;

import factory.example.ingredients.cheese.Cheese;
import factory.example.ingredients.clam.Clam;
import factory.example.ingredients.dough.Dough;
import factory.example.ingredients.sauce.Sauce;
import factory.example.ingredients.veggie.Veggie;

public interface IngredientFactory {
  Dough createDough();
  Sauce createSauce();
  Cheese createCheese();
  Veggie[] createVeggies();
  Pepperoni createPepperoni();
  Clam createClam();
}
