public class Main {
  public static void main(String[] args) {
    final CaffineBeverage coffee = new Coffee();
    final CaffineBeverage tea = new Tea();

    coffee.prepareRecipe();
    tea.prepareRecipe();
  }
}