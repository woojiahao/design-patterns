package decorator.example.condiments;

import decorator.example.beverages.Beverage;

public class Mocha extends CondimentDecorator {
  private Beverage beverage;

  public Mocha(Beverage beverage) {
    super("Mocha");
    this.beverage = beverage;
  }

  @Override public String getDescription() {
    return beverage.getDescription() + ", Mocha";
  }

  @Override public double cost() {
    return beverage.cost() + 0.49;
  }
}
