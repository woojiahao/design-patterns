# Decorator Pattern
## Definition
> Decorator Pattern attaches additional responsibilities to an object dynamically. Decorators provide a flexible 
> alternative to subclassing for extending functionality.

## Design principles
* Classes should be open for extension, but closed for modification 

## Scenario
Say we begin developing a coffee shop system and we start with having a base parent class called `Beverage` for all 
variations of the beverage to extend from.

```java
abstract class Beverage {
  private String description;

  Beverage(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public abstract double cost();
}
```

The `cost()` method will be overwritten in child classes to derive the cost of the beverage. An example of a child 
class can be seen below:

```java
class Mocha extends Beverage {
  Mocha() {
    super("Mocha");
  }

  @Override public double cost() {
    return 4.5;
  }
}
```

For a handful of beverages, this pattern seems alright, not too complex and we can maintain the code base quite easily.
However, the problem arises if we begin to introduce variations into the types of beverages and have various toppings:

```java
class MochaWithCream extends Beverage { }

class MochaWithCreamAndCaramel extends Beverage { }

class MochaWithCaramel extends Beverage { }

class DecafWithCream extends Beverage { }
```

As you can see, the number of classes instantly explodes and it now becomes even harder to maintain because we now not 
only have to maintain all sorts of combinations of what is essentially the same class, but also, when we calculate the
cost of the beverage, it will use the following formula: `base cost + condiments` and the `condiments` should always 
remain the same. You know where I'm going - if we decide that the cost of `Cream` changes from $0.30 to $0.50, we would 
have to go into each combination that does include the `Cream` and edit the formula for calculating the cost of the 
beverage.

This solution is messy and very difficult to maintain. So let's explore some alternatives.

### Separating condiments?
Applying what we learnt from the other design patterns, we first look at what differs and attempt to encapsulate it. As
what was brought up earlier, the issue of maintaining the condiments and having too many subclasses to properly manage
the various combinations of condiments to drinks differ too much and make it hard to maintain the code. As such, we can
encapsulate the condiments into the parent `Beverage` class and make use of some inheritance tricks to cut down the 
class footprint and to improve manageability.

```java
class Beverage {
  // ... Other variables
  private boolean milk;
  private boolean soy;
  private boolean cream;
  private boolean caramel;

  // ... Other methods

  // Repeat getters/setters for each condiment type
  public getMilk() {
    return milk;
  }

  public setMilk(boolean milk) {
    this.milk = milk;
  }

  public double cost() {
    final double MILK_COST = 0.30;
    final double SOY_COST = 0.30;
    final double CREAM_COST = 0.50;
    final double CARAMEL_COST = 0.70;
    
    double totalCost = 0.0;
    if (milk) totalCost += MILK_COST;
    if (soy) totalCost += SOY_COST;
    if (cream) totalCost += CREAM_COST;
    if (caramel) totalCost += CARAMEL_COST;

    return totalCost;
  }
}
```

To achieve the encapsulation, the parent `Beverage` class holds onto boolean states that represent whether the beverage
has a specific type of condiment. Then, instead of allowing the `cost()` method to be overwritten by subclasses to 
work with the specific implementation, we implement a version of `cost()` within the parent class and have it calculate
the total cost of the condiments. So if we were to subclass the `Beverage` class, all we need to do is to supplement the
base cost of the type of beverage then call the **super** implementation of `cost()` to get the total cost of the 
condiments.

```java
class Mocha extends Beverage {
  @Override public double cost() {
    double condimentsCost = super.cost();
    return 1.45 + condimentsCost;
  }
}
```

With this design, it's a lot easier to manage the subclasses and if the price of a condiment changes, we only need to
modify a single location. This is great!

```java
// Creating the same Mocha as the MochaWithCreamAndCaramel
Beverage mocha = new Mocha();
mocha.setCream(true);
mocha.setCaramel(true);
System.out.println(mocha.cost());
```

However, there are some flaws with this design.

1. If we have a new price for the condiments, we will have to edit existing code, which can introduce breaking changes
2. If we add a new condiment, we will have to modify 4 locations in `Beverage` - a new boolean, setter and getter and 
   adding the new condiment price in `cost()`
3. For some beverages, it might not make sense to include a certain type of condiment, but we're forced to allow users
   to include these condiments because of the class design
4. There are some beverages like the `DoubleMocha` that cannot be done using this system

This design principle summarises the key flaws with the design:

> Classes should be open for extension, but closed for modification

The key idea behind this design pattern is the idea that when we ensure that the class is not open to modifications 
(like if we try to introduce a new condiment), we lower the risk of introducing a new bug into the system that might 
mess with the rest of the components of the existing class. Instead, if we just extend the functionality and add the 
condiment in that fashion, we would only have a single class to worry about causing troubles.

However, as with any design pattern, we must use it sparingly as attempting to apply this principle to every class will
be too tedious.

### Decorators as wrappers...
In order to wrap (pun intended!) our heads around the idea of the decorator pattern, we have to first look at a use case
of our current system and see how we can make use of the decorator pattern to help us.

Say we want a **Dark Roast with Mocha and Cream**. What are the steps we should take to represent this object:

1. Start with a `DarkRoast` object
2. Decorate it with `Mocha`
3. Decorate it with `Cream`
4. When calling `cost()`, rely on delegation to add the cost of condiments

Let's look at the last step and learn what "rely on delegation" means. As we decorate the base object, it gets nested 
deeper and deeper. When we finally call `cost()`, it'll actually be called on the outermost object, which in this 
scenario would be the `Cream`. Then, the call stack would propagate to each class this outermost object wraps around 
until it reaches the original base object, where everything gets returned up again.

```
Cream (cost()) -> Mocha (cost()) -> DarkRoast (cost() + returns baseCost) -> Mocha (adds condiment to baseCost) -> Cream (adds condiment to baseCost)
```

From this, we know several things about the decorator pattern:

* Decorators are the same supertype as the objects they are decorating - if `Mocha` decorates `DarkRoast`, `Mocha` is 
  also a subclass of `Beverage`
* One or more decorators can be used to decorate an object
* Because the decorator is the same supertype as the objects they wrap, they can be used in place
* The decorator adds its own behaviour either before and/or after delegating to the object it decorates to do the rest
  of its job
* Objects can be decorated at any time, even during runtime
