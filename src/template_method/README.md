# Template Method Pattern
## Description


## Design Patterns

## Scenario
Say we are creating a coffee shop system and we are writing instructions for the drink making system. We first start with the instructions for coffee brewing. In order to create the instructions, we create a class for the drink and the instructions are housed as methods. 

```java
public class Coffee {
  void prepareRecipe() {
    boilWater();
    brewCoffeeGrinds();
    pourInCup();
    addSugarAndMilk();
  }

  public void boilWater() {
    System.out.println("Boiling water");
  }

  public void brewCoffeeGrinds() { 
    System.out.println("Dripping Coffee through filter")
  }

  public void pourInCup() {
    System.out.println("Pouring into cup");
  }

  public void addSugarAndMilk() {
    System.out.println("Adding Sugar and Milk");
  }
}
```

Now, we want to create another one for making tea. So we devise a `Tea` class to include the instructions as well.

```java
public class Tea {
  void prepareRecipe() {
    boilWater();
    steepTeaBag();
    pourInCup();
    addLemon();
  }

  public void boilWater() {
    System.out.println("Boiling water");
  }

  public void steepTeaBag() {
    System.out.println("Steeping the tea");
  }

  public void addLemon() {
    System.out.println("Adding Lemon");
  }

  public void pourInCup() {
    System.out.println("Pouring into cup");
  }
}
```