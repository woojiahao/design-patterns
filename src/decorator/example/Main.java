package decorator.example;

import decorator.example.beverages.Beverage;
import decorator.example.beverages.Espresso;
import decorator.example.beverages.HotBlend;
import decorator.example.condiments.Mocha;
import decorator.example.condiments.Whip;

public class Main {
  public static void main(String[] args) {
    // Undecorated item
    Beverage espresso = new Espresso();
    System.out.println(espresso.getDescription() + " " + espresso.cost());

    // Decorated item
    Beverage hotBlend = new HotBlend();
    hotBlend = new Mocha(hotBlend);
    hotBlend = new Whip(hotBlend);
    System.out.println(hotBlend.getDescription() + " " + hotBlend.cost());
  }
}
