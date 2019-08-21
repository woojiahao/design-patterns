# Template Method Pattern
## Description
> Template Method Pattern defines the skeleton of an algorithm in a method, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.

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

As you can see there is some code duplication going on here where the steps to boil water and pour the drink into the cup are repeated for both the coffee and tea. While this might be forgiveable while there are only a handful of drinks, it is best that we reduce code duplication - **abstracting the commonality into a base class**.

### CaffineBeverage 
In order to create this abstraction, we will create an abstract class to house all of the common points of the drinks. 

```java
public class CaffineBeverage {
  abstract void prepareRecipe();
  
  void boilWater() {
    System.out.println("Boiling water");
  }

  void pourInCup() {
    System.out.println("Pouring into cup");
  }
}

public class Coffee extends CaffineBeverage {
  @Override void prepareRecipe() {
    boilWater();
    brewCoffeeGrinds();
    pourInCup();
    addSugarAndMilk();
  }

  // ...
}
```

We can further abstract this by using generic names for methods like `Coffee#brewCoffeeGrinds` and `Tea#steepTeaBag` to something like `Coffee#brew` and `Tea#brew`.

This way, we can create abstract methods within our `CaffineBeverage` class and have that be the one to handle the preparation. So we effectively remove the need for each subclass to implement their own version of the `CaffineBeverage#prepareRecipe`.  Instead, each subclass provides the implementation of the methods that vary such as `brew` and `addCondiments`.

```java
public class CaffineBeverage {
  void prepareRecipe() {
    boilWater();
    brew();
    pourInCup();
    addCondiments();
  }

  // ... boilWater and pourInCup from last example

  abstract void brew();
  abstract void addCondiment();
}

public class Coffee extends CaffineBeverage {
  @Override void brew() {
    System.out.println("Dripping coffee through filter");
  }

  @Override void addCondiment() {
    System.out.println("Adding sugar and milk");
  }
}

public class Tea extends CaffineBeverage {
  @Override void brew() {
    System.out.println("Steeping the tea");
  }

  @Override void addCondiments() {
    System.out.println("Adding lemon");
  }
}
```

What have done is essentially create a template method where the `CaffineBeverage#prepareRecipe` is the template and any subclass follows the template by implementing the methods needed to be supplied. This ensures that as long as we make a subclass of `CaffineBeverage`, we do not exclude an instruction in the `prepareRecipe` if we had stuck to our earlier design of the application.

Each subclass supplies implementations of primitive operations and the abstract class will use each implementation in the template method.