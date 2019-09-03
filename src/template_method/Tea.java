public class Tea extends CaffineBeverage {
  @Override void brew() {
    System.out.println("Brewing tea");
  }

  @Override void addCondiments() {
    System.out.println("Adding creamer");
  }
}