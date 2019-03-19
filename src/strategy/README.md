# Strategy Pattern
## Definition
> Strategy pattern defines a family of algorithms, encapsulating each one, and makes them interchangeable. Strategy 
> lets the algorithm very independently from clients that use it.

## Design principles
* [Encapsulate what varies](https://github.com/woojiahao/design-patterns/tree/master/src/strategy#leveraging-encapsulation)
* [Favor composition over inheritance](https://github.com/woojiahao/design-patterns/tree/master/src/strategy#wrapping-up)
* [Program to interfaces, not implementations](https://github.com/woojiahao/design-patterns/tree/master/src/strategy#adding-more-flexibility)

## Scenario
A duck super class (`AbstractDuck`) holds onto information that all Duck children should have, such as `quack()` which 
are pieces of information relating to the Duck. Each subclass of the `AbstractDuck` will extend this parent and 
implement their versions of the various abstract methods found within the `AbstractDuck` class.

```java
abstract class AbstractDuck {
  String name;

  abstract void quack();
}
```

An example implementation of the `AbstractDuck` can be examined using a `MallardDuck`.

```java
class MallardDuck extends AbstractDuck {
  MallardDuck() {
    name = "Mallard";
  }

  @Override void quack() {
    System.out.println(name + " says quack!");
  }
}
```

This is all fine and dandy until you notice that if you were to add a new piece of behavior for `AbstractDuck` all 
subclasses of this super class has to now implement this piece of behavior without fail (remember that an `abstract`
method must be overwritten). 

```java
abstract class AbstractDuck {
  // ...
  abstract void fly();
}
```

```java
class MallardDuck extends AbstractDuck {
  MallardDuck() {
    name = "Mallard";
  }

  @Override void quack() {
    System.out.println(name + " says quack!");
  }
  
  @Override void fly() {
    System.out.println(name + " soars into the air.");
  }
}
```

> Although the use of inheritance improved a potential situation with reuse, it caused the program to be less 
> maintainable.

Another problem soon arises, and when new types of Ducks are added, they introduce corner cases that require a lot of 
fiddling with the code to fit into our existing solution built upon inheritance. For instance, if a user decides to
add a `RubberDuck` to the list of children.

```java
class RubberDuck extends AbstractDuck {
  RubberDuck() {
    name = "Rubber duck";
  }
  
  @Override void quack() {
    // A rubber duck can't quack, so I'll make it squeak instead
    System.out.println("Squeak!");
  }
  
  @Override void fly() {
    // A rubber duck can't fly either, I guess I'll leave this empty
  }
}
```

### Problems identified
Notice that there are several problems with the current implementation highlighted when you bring in the `RubberDuck` 
implementation.

1. Even though a `RubberDuck` cannot quack, its implementation is forced to override that method because it has to 
inherit from `AbstractDuck` - this makes for unpredictable behavior since not everyone using the `RubberDuck` class
will expect a `RubberDuck` to squeak when called to quack!
2. Even though a `RubberDuck` cannot fly, its implementation is once again forced to override that method since it is 
inheriting from `AbstractDuck` - and in this implementation, we just leave the `fly()` method blank since it does 
nothing, and users of the class might not predict this behavior.

Furthermore, other problems regarding maintenance arises as you begin scaling the application with more Ducks!

1. If we added more Ducks, we would need to override the same methods over and over again and make small minute changes
to the methods even if the general idea remains unchanged.

    ```java
    class RedDuck extends AbstractDuck {
      RedDuck() {
        name = "Red";
      }

      @Override void quack() {
        // I don't need to do anything special, this is the exact same 
        // thing as what the MallardDuck does when it quacks!
        System.out.println(name + " says quack!");
      }

      @Override void fly() { // ...
      }
    }
    ```

    Imagine doing this for another 30 set of ducks.

    This problem also appears when mimicking Ducks like the `RubberDuck`, where a certain method will not perform any
    action. 
    
    Is it really that smart to have so much duplicated code? Wasn't the point of using inheritance to reduce the 
    amount of code duplication we have to perform?

### Using interfaces?
A naive solution that would solve the problem of corner cases would be to extract these variadic behaviors and storing
them into interfaces that the children can implement if the specific behavior applies. 

For instance, we can move the `fly()` behavior into an interface called `IFlyable` and have only those Ducks that can 
fly implement this interface. 

```java
interface IFlyable {
  void fly();
}

class MallardDuck extends AbstractDuck implements IFlyable { }

class RubberDuck extends AbstractDuck { }
```

Perfect! Now if a new Duck child doesn't have the ability to fly, it can simply not implement the `IFlyable` interface
and it will be spared of the forced implementation.

**However**, only half the problem is resolved, whilst it is true that we no longer have to worry about managing 
multiple pieces of corner cases, we have yet to resolve a fundamental problem we had initially adopted inheritance to 
solve - **code duplication!**

### Leveraging encapsulation
> Encapsulation involves the idea of bundling data and methods so that only the necessary details are exposed to users

The first step in achieving "code zen" is to identify the components in our code that varies and to encapsulate it, so
that it is as far away as possible from the rest of our code. Or to put it nicely:

> Identify the aspects of your application that vary and separate them from what stays the same

Doing this simple step will enable us to reduce the amount of potential bugs. Imagine this, instead of managing 20 
places that consist of the same piece of code, we can manage 1 place - implementing the changes here, and have it
influence the 20 places that relies on this piece of code.

In the case of our Duck scenario, we first have to look at the pieces that change, and extract that away from our 
`AbstractDuck` class. From our prior analysis, you'll notice that the `fly()` and `quack()` behaviors vary the most 
between Duck implementations, so we will extract those into their own classes, completely separate from the 
`AbstractDuck` class.

```java
interface IFlyBehavior {
  void fly(String name);
}

interface IQuackBehavior {
  void quack(String name);
}

class FlyWithWings implements IFlyBehavior {
  @Override void fly(String name) {
    System.out.println(name + " soars in the air!");
  }
}

class FlyNoWay implements IFlyBehavior {
  @Override void fly(String name) {
    // Does nothing because this class doesn't fly
  }
}
```

And with the new design, we can now provide add this "extension" to `AbstractDuck`:

```java
abstract class AbstractDuck {
  IFlyBehavior flyBehavior;
  IQuackBehavior quackBehavior;
  String name;

  public fly() {
    flyBehavior.fly(name);
  }

  public quack() {
    quackBehavior.quack(name);
  }
}
```

Now, when we create a new Duck child, instead of adding the specific behavior to each new child, we can simply specify
a pre-defined class implementing the behavior and have the base `AbstractDuck` class be the one to decide what the Duck
child does, simply by calling the respective methods for that behavior.

```java
class MallardDuck extends AbstractDuck {
  MallardDuck() {
    flyBehavior = new FlyWithWings();
  }
}

class RubberDuck extends AbstractDuck {
  RubberDuck() {
    flyBehavior = new FlyNoWay();
  }
}
```

And with that, the behavior is encapsulated within a set of classes that can be maintained separately from the Duck
children. If we want to add a new set of variable behaviors, we can just add a new interface and have other classes
implement that interfaces without having to edit God knows how many sub classes and repeating God knows how many 
common implementations.

If a developer is using a `AbstractDuck` child, they do not need to be bothered with the implementation of any 
behavior, all they need to know is that the Duck can `fly` or `quack` and that's all they should care about.

### Adding more flexibility
Right now, our current solution is quite adequate, however, we can always improve it. An aspect we have neglected is 
the extensibility of our `AbstractDuck` class. We're restricted to the behavior set during compile time (aka the 
behavior we specified in the constructor of the sub classes). 

If we want to ensure that the class is flexible, we should introduce the ability for this behavior to change during 
runtime and the most intuitive way to do this is via a getter/setter.

This idea adheres to another design principle:

> Program to an interface, not an implementation

This principle is actually applied in 2 areas of our solution.

1. In the previous iteration of our solution, the behavior of a Duck was programmed to an implementation - we hardcoded
   each behavior and was stuck with it even during runtime
2. When we create a Duck object, we hardcoded the type of of Duck via `MallardDuck mDuck = new MallardDuck();` which
   reduces flexibility since we have essentially bottlenecked ourself to only be able to reassign `mDuck` to other 
   variables of type `MallardDuck`.

   The better alternative would have been: `AbstractDuck mDuck = new MallardDuck();`.

In both scenarios, the term `interface` simply refers to the polymorphic behavior most OOP languages offers and that is
to allow a supertype to be assigned to subtypes so that the actual runtime code isn't locked in to a specific 
implementation.

To address the first issue, we can introduce setters for the behaviors in `AbstractDuck` so that users can change the 
behaviors during runtime if necessary.

```java
abstract class AbstractDuck {
  private IFlyBehavior flyBehavior;

  public void setFlyBehavior(IFlyBehavior flyBehavior) {
    this.flyBehavior = flyBehavior;
  }
}

public static void main(String[] args) {
  AbstractDuck mDuck = new MallardDuck();
  mDuck.fly(); // Should be FlyWithWings implementation
  mDuck.setFlyBehavior(new FlyNoWay());
  mDuck.fly(); // Should be FlyNoWay implementation
}
```

### Cleaning up code
Since the `AbstractDuck` class no longer requires sub classes to inherit abstract methods, it can be stripped of the 
abstract class naming convention. We also properly encapsulate the name variable.

```java
class Duck {
  private IFlyableBehavior flyBehavior;
  private IQuackableBehavior quackBehavior;
  private String name;

  Duck(String name, IFlyableBehavior flyBehavior, IQuackableBehavior quackBehavior) {
    this.name = name;
    this.flyBehavior = flyBehavior;
    this.quackBehavior = quackBehavior;
  }

  void quack() {
    quackBehavior.quack(name);
  }

  void fly() {
    flyBehavior.fly(name);
  }

  void setFlyBehavior(IFlyableBehavior flyBehavior) {
    this.flyBehavior = flyBehavior;
  }

  void setQuackBehavior(IQuackableBehavior quackBehavior) {
    this.quackBehavior = quackBehavior;
  }
}

```

### Wrapping up
Instead of looking at this program as a set of behaviors, it can be thought of as a family of algorithms, where the 
program represents the various things the duck can do.

In fact, what we have performed is to **favor composition over inheritance**. **Composition** refers to a *HAS-A* 
relationship whereas **inheritance** refers to a *IS-A* relationship. Instead of a Duck inheriting some set of behavior,
the Duck is composed of this set of behavior.

> Favor composition over inheritance

By favoring composition, we also made the program more flexible as now it isn't programmed to an implementation but
rather programmed to an interface where the behaviors can be changed during runtime if necessary.

Applying this design pattern has enabled us to create a program that is not only easy to setup but also easy to 
maintain, establishing a balance between clever design and a practical one.