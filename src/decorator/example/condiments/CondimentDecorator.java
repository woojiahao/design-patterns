package decorator.example.condiments;

import decorator.example.beverages.Beverage;

public abstract class CondimentDecorator extends Beverage {
  public CondimentDecorator(String description) {
    super(description);
  }

  public abstract String getDescription();
}
