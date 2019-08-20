package decorator.example.beverages;

public class HotBlend extends Beverage {
  public HotBlend() {
    super("Hot Blend");
  }

  @Override public double cost() {
    return 0.89;
  }
}
