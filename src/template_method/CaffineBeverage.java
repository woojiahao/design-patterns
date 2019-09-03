public abstract class CaffineBeverage {
  public void prepareRecipe() {
    boilWater();
    brew();
    pourInCup();
    if (wantsCondiments()) {
      addCondiments();
    }
  }

  void boilWater() {
    System.out.println("Boiling water");
  }

  abstract void brew();
  
  void pourInCup() {
    System.out.println("Pouring into cup");
  }

  abstract void addCondiments();

  boolean wantsCondiments() {
    return true;
  }
}