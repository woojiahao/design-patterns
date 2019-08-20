# Decorator Pattern
## Definition
> Decorator Pattern attaches additional responsibilities to an object dynamically. Decorators provide a flexible 
> alternative to subclassing for extending functionality.

## Design principles
* [Classes should be open for extension, but closed for modification](https://github.com/woojiahao/design-patterns/tree/master/src/decorator#separating-condiments) 

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
Cream (cost()) 
  -> Mocha (cost()) 
    -> DarkRoast (cost() + returns baseCost) 
      -> Mocha (adds condiment to baseCost) 
        -> Cream (adds condiment to baseCost)
```

From this, we know several things about the decorator pattern:

* Decorators are the same supertype as the objects they are decorating - if `Mocha` decorates `DarkRoast`, `Mocha` is 
  also a subclass of `Beverage`
* One or more decorators can be used to decorate an object
* Because the decorator is the same supertype as the objects they wrap, they can be used in place
* The decorator adds its own behaviour either before and/or after delegating to the object it decorates to do the rest
  of its job
* Objects can be decorated at any time, even during runtime

### Designing decorators!

Each decorator created has a **HAS-A** relationship to the supertype, holding a reference to this. In our scenario, the
base decorator will represent the condiments that drinks can have.

```java
class CondimentDecorator extends Beverage {
  private Beverage wrapped;

  CondimentDecorator(Beverage wrapped) {
    this.wrapped = wrapped;
  }
}
```

The other drinks remain as subclasses of the `Beverage` class whilst each individual condiment subclasses the 
`CondimentDecorator` class instead.

```java
class Milk extends CondimentDecorator { }

class Whip extends CondimentDecorator { }
```

And now you might be wondering, isn't this already violating the OG design principle of "Favour composition over 
inheritance". The answer to that is both yes *and* no. Whilst we are building the system upon inheritance, it is an
appropriate use case for it and we do not solely rely on inheritance to get the job done. We intentionally include a 
**HAS-A** relationship between the decorator and the supertype by holding an instance of this supertype within itself.
The code is free from modifications after the fact and we can always add more condiments or beverage types without 
touching the original `Beverage` class. 

### Tying it all together
Now that we have a general understanding of the decorator pattern and how it ties into our application, we can start to 
implement it.

We first start by building out the base `Beverage` class, nothing much has changed since the last time.

```java
abstract class Beverage {
  private String description;

  Beverage(String description) {
    this.description = description;
  }

  String getDescription() { return description; }

  abstract double cost();
}
```

Then, we create our decorator for condiments, `CondimentDecorator`. Here, we intentionally set the `getDescription()`
method to be abstract as we want each condiment subtype to implement their own version of it.

```java
abstract class CondimentDecorator extends Beverage {
  CondimentDecorator(String description) { super(description); }

  abstract String getDescription();
}
```

We can then begin to build the various beverages.

```java
class Espresso extends Beverage {
  Espresso() { super("Espresso"); }

  @Override public double cost() { return 1.99; }
}

class HouseBlend extends Beverage {
  HouseBlend() { super("House Blend"); }

  @Override public double cost() { return 0.89; }
}
```

Up till now, most of our code remains fairly untouched, with most just a repeat of the original design. But now that we
have the `CondimentDecorator` class, we can exploit the decorator pattern and begin adding condiments. 

```java
class Whip extends CondimentDecorator {
  private Beverage beverage;

  Whip(Beverage beverage) { 
    super("Whip");
    this.beverage = beverage;
  }

  @Override public String getDescription() {
    return beverage.getDescription() + ", Mocha";
  }

  @Override public double cost() {
    return beverage.cost() + 0.20;
  }
}
```

Notice how we rely on the given `beverage`'s prior value to build upon what the condiment's cost and description is.
This gives us the ability to backtrack through a whole set of nesting to retrieve the necessary information for our use.

Let's see how we can use this new system...

```java
Beverage espresso = new Espresso();
System.out.println(espresso.getDescription());

Beverage houseBlend = new HouseBlend();
houseBlend = new Whip(houseBlend);
houseBlend = new Mocha(houseBlend);
System.out.println(houseBlend.getDescription());

> Espresso
> House Blend, whip, mocha
```

As you can see, the `houseBlend` beverage is decorated with various condiments that reflects when retrieving the 
description.

### Decorative flaws...
Whilst the decorator pattern is quite versatile, it comes with a bevy of flaws.

* **Modifying the base beverages** - if we decide that the `Espresso` will have a discount, this discount is not going 
  to be properly propagated to any condiments that decorate it, which restricts our application
* **Managing more objects** - introducing the decorator pattern and way of thinking causes us to have a management
  problem on our hands as we have to now think about the ordering of the decorations etc, however, this can be resolved
  since most applications of the decorator pattern is backed by other design patterns
* **Parsing descriptions** - if we have a special formatting for our descriptions, it will take some reworking to 
  implement it, for instance, if we have 2 occurrences of `Mocha` in the decorated object, we would want to have the 
  string `Double Mocha` instead of `Mocha, Mocha`
* **Too many decorations** - as the number of decorators increase, so does the complexity of the application as it gets
  harder for the developer to know exactly what to decorate their objects with in order to achieve the behavior they
  want

### I/O decorations!
Now let's explore some uses of the decorator pattern within the Java API, more specifically, how it is used with the
I/O API, starting with `FileInputStream`. 

`FileInputStream` provides a base for all other I/O components. It is decorated by these other components to extend its
functionality. So what exactly are the components that decorate the `FileInputStream`? Well, for this demonstration, 
we are looking at the base decorator class of `FilterInputStream` with subclasses like `BufferedInputStream` and 
`DataInputStream`.

The book goes into detail about creating a custom `FilterInputStream` which can be found in the folder `io`.