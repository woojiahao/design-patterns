package decorator.example.condiments;

import decorator.example.beverages.Beverage;

public class Whip extends CondimentDecorator {
  private Beverage beverage;

  public Whip(Beverage beverage) {
    super("Whip");
    this.beverage = beverage;
  }

  @Override public double cost() {
    return beverage.cost() + 1.39;
  }

  @Override public String getDescription() {
    return beverage.getDescription() + ", Whip";
  }
}
