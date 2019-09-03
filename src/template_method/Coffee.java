import java.util.Scanner;

public class Coffee extends CaffineBeverage {
  @Override void brew() {
    System.out.println("Brewing coffee");
  }

  @Override void addCondiments() {
    System.out.println("Adding milk");
    System.out.println("Adding creamer");
  }

  @Override boolean wantsCondiments() {
    final Scanner userInput = new Scanner(System.in);
    System.out.println("Do you wish to add condiments?");
    final String response = userInput.nextLine();
    return response.equalsIgnoreCase("yes");
  }
}