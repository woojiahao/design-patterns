# Template Method Pattern
## Description
> Template Method Pattern defines the skeleton of an algorithm in a method, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.

## Design Patterns
- Don't call us, we'll call you

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
public abstract class CaffineBeverage {
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
public abstract class CaffineBeverage {
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

### Introducing hooks
> A hook is a method that is declared in the abstract class but is only given an empty of default implementation.

Making use of hooks allows the subclasses to hook into the algorithm at various points. The subclass is free to ignore the hook as well if they desire.

In the case of our coffee machine, we can create a hook to allow the subclass to specify whether the customer wants condiments or not before we invoke the `CaffineBeverageWithHook#addCondiments` method. This is done so that we can have minute control over the configuration of the beverages while maintaining the control with the super class - preventing the subclasses from modifying the build process.

```java
public abstract class CaffineBeverageWithHook {
  final void prepareRecipe() {
    boilWater();
    brew();
    if (customerWantsCondiments()) {
      addCondiments();
    }
  }

  boolean customerWantsCondiments() {
    return true;
  }
}
```

We can then hook in a system where the user is prompted for whether or not they want condiments and based on their response, the subclass overrides the return value of the `CaffineBeverageWithHook#customerWantsCondiments` with the respective user response. 

```java
public class Coffee extends CaffineBeverageWithHook {
  public boolean customerWantsCondiments() {
    String userResponse = getUserInput(); // Method asks if the user wishes to include condiments
    return userResponse.toLowerCase().startsWith("y");
  }
}
```

As you can see, the coffee class is now able to control whether or not the drink that is being prepared contains condiments or not. 

### Hollywood Principle
> Don't call us, we will call you.

The Hollywood principle gives us  way to prevent "dependency rot". Dependency rot happens when there are high-level components depending on sideways components depending on low-level components and so on. When this sets in, no one can easily understand the way a system is designed.

With the Hollywood Principle, low-level components can hook themselves into the system, but the high-level components determine when they are needed, and how. This means that high-level components will initiate contact with the low-level components.

In this scenario, the `CaffineBeverage` is the high-level component and it is the one with the control over the algorithm for the recipe. It will only call onto the subclass when it needs the implementation of a method. The other classes simply provide implementation details for this high-level component.

### Real world application
One real world application of the template pattern can be found in the Java Arrays class which provides a template method for sorting and this is done using the `Comparable` interface. Subclasses implementing this will use the method `compareTo` to sort itself against other classes such as itself.

Next, when we have a collection of classes implementing `Comparable`, we rely on the implementation of its `compareTo` to sort the items.

Going off of the real world Java API, hooks are also demonstrated through classes like applets where hooks such as `destroy` are found and they are used to specify the behavior of the class when it is being destroyed.