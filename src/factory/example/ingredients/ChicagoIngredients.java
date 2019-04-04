package factory.example.ingredients;

import factory.example.ingredients.cheese.BlueCheese;
import factory.example.ingredients.cheese.Cheese;
import factory.example.ingredients.clam.Clam;
import factory.example.ingredients.dough.Dough;
import factory.example.ingredients.dough.StuffedCrustDough;
import factory.example.ingredients.sauce.Sauce;
import factory.example.ingredients.veggie.Broccoli;
import factory.example.ingredients.veggie.Veggie;

public class ChicagoIngredients implements IngredientFactory {
  @Override public Dough createDough() {
    return new StuffedCrustDough();
  }

  @Override public Sauce createSauce() {
    return new Sauce();
  }

  @Override public Cheese createCheese() {
    return new BlueCheese();
  }

  @Override public Veggie[] createVeggies() {
    return new Veggie[] { new Broccoli(), new Broccoli() };
  }

  @Override public Pepperoni createPepperoni() {
    return new Pepperoni();
  }

  @Override public Clam createClam() {
    return new Clam();
  }
}
