package decorator.example.beverages;

public abstract class Beverage {
  private String description;

  public Beverage(String description) {
    this.description = description;
  }

  abstract public double cost();

  public String getDescription() {
    return description;
  }
}
