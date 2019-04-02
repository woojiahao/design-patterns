package factory.example.pizza;

public class ChicagoStyleCheesePizza extends Pizza {
  public ChicagoStyleCheesePizza() {
    super(
      "Chicago Style Deep Dish Cheese Pizza",
      "Extra Thick Crust Dough",
      "Plum Tomato Sauce",
      "Shredded Mozzarella Cheese"
    );
  }

  @Override public void cut() {
    System.out.println("Cutting the pizza into square slices");
  }
}
