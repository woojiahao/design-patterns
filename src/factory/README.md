# Factory Pattern
## Definition
### Factory method pattern
> Factory method pattern defines an interface for creating an object, but lets subclasses decide which class to 
> instantiate. Factory Method lets a class defer instantiation to subclass.

### Abstract factory pattern
> Abstract factory pattern provides an interface for creating families of related dependent objects without specifying
> their concrete classes.

## Design Principles 
* Depend on abstractions. Do not depend on concrete classes.

## Scenario
A previous design pattern brought up is the idea of **Programming to an interface, not implementation** and you might be 
wondering, "If we want to always program to an interface, isn't `Duck mallardDuck = new MallardDuck()` programming to an
implementation?" The answer to that is yes. However, this is the best way we grant flexibility in the event where we 
want to do something like

```java
Duck duck;

if (picnic) {
  duck = new MallardDuck();
} else if (hunting) {
  duck = new DecoyDuck();
} else {
  duck = new RubberDuck();
}
```

We can assign `duck` to any form of `Duck` depending on some condition that is given. The issue with this code, however,
is just how brittle it is and error-prone. If we decided to add a new condition that created a different duck, we would
need to modify the existing code, violating the design principle of **keeping a class open for extension but closed for
modification.**

Now that we have outlined a basic problem, let's dive into the chapter's primary example - Pizza!!

We first begin with examining how our basic system works, we first have some input (String) to a method that then looks
at this String and determines what type of pizza to create. Then we prepare the pizza and return it.

```java
Pizza orderPizza(String type) {
  Pizza pizza;

  if (type.equals("cheese")) {
    pizza = new CheesePizza();
  } else if (type.equals("greek")) {
    pizza = new GreekPizza();
  } else if (type.equals("pepperoni")) {
    pizza = new PepperoniPizza();
  }

  pizza.prepare();
  pizza.bake();
  pizza.cut();
  pizza.box();
  return pizza;
}
```

This system mirrors the plight we were in with the ducks, if we changed up some of the types, we would have to modify 
the existing code to match the changes, introducing potential bugs into the system.

However, we also notice that whilst we change the if statement code, the rest of the code remains fairly untouched. 
This is where the design principle of **encapsulating what changes** kicks in as we can clearly abstract the decision
of what pizza to another class like a `PizzaFactory`. So we start by creating that.

### Pizza factories!
```java
class PizzaFactory {
  Pizza createPizza(String type) {
    Pizza pizza;

    if (type.equals("cheese")) {
      pizza = new CheesePizza();
    } else if (type.equals("greek")) {
      pizza = new GreekPizza();
    } else if (type.equals("pepperoni")) {
      pizza = new PepperoniPizza();
    }

    return pizza;
  }
}
```

And after creating our `PizzaFactory` we are able to use it when ordering pizzas.

```java
Pizza orderPizza(String type) {
  Pizza pizza = new PizzaFactory().createPizza(type);
  // ...
  return pizza;
}
```

What we have done is essentially encapsulated our varying behavior into a class and method of its own. This provides us
with the advantages discussed many times before, but I'll reiterate:

1. **Code reuse** - we can reuse the PizzaFactory in multiple places and we know we will get the same behavior 
   throughout
2. **Maintenance** - since we encapsulated the behavior and use this encapsulated API in multiple places, we only have 
   to worry about modifying a single location for changes to be propagated to all the related classes
3. **Inheritance** - by allowing the factory to be a class, we allow it to be extended by other classes and uesd in 
   more diverse ways

### Expanding business...
Now, you want to have pizza factories in different countries, so we begin with expanding our system to match these 
requirement changes.

```java
class NYPizzaFactory extends PizzaFactory {
  @Override Pizza createPizza(String type) { }
}

class ChicagoPizzaFactory extends PizzaFactory {
  @Override Pizza createPizza(String type) { }
}

PizzaFactory nyPizzaFactory = new NYPizzaFactory();
PizzaStore nyStore = new PizzaStore(nyPizzaFactory);
nyStore.orderPizza("pepperoni");

PizzaFactory chicagoPizzaFactory = new ChicagoPizzaFactory();
PizzaStore chicagoStore = new PizzaStore(chicagoPizzaFactory);
chicagoStore.orderPizza("pepperoni");
```

However, there's an issue about this system, the changes that are made are not localised to the pizza store. This means
if the store decides to create the pizza differently from the default factory, we would need to create a new subclass
of the `PizzaFactory` to handle the changes, not too ideal for maintenance.

To rectify this problem, we can move the `createPizza` behavior into the PizzaStore so each store can handle the 
creation differently.

```java
abstract class PizzaStore {
  Pizza orderPizza(String type) {
    Pizza pizza = createPizza(type);
    //...
    return pizza;
  }

  abstract Pizza createPizza(String type);
}
```

Now with this, each pizza store can handle the pizza creation a little differently, and it's not tied down to the type
of pizza factory; eliminating the issue encountered earlier.

This design decouples the implementation of the `createPizza` from the pizza store, so when we operate on the pizza by
using methods like `pizza.cut()`, we do not need to know what type of pizza was returned, all we know is that we have
to operate on a pizza.

In this scenario, the abstract `createPizza` method is a factory method.

### Making pizzas 🤤
Let's first begin to design the various pizzas

```java
public abstract class Pizza {
  private String name;
  private String dough;
  private String sauce;
  private List<String> toppings;

  public Pizza(String name, String dough, String sauce, String... toppings) {
    this.name = name;
    this.dough = dough;
    this.sauce = sauce;
    this.toppings = Arrays.asList(toppings);
  }

  public void prepare() {
    System.out.println("Preparing " + name);
    System.out.println("Tossing dough...");
    System.out.println("Adding sauce...");
    System.out.println("Adding toppings:");
    toppings.forEach(topping -> System.out.println("\t" + topping));
  }

  public void bake() {
    System.out.println("Bake for 30 minutes at 350");
  }

  public void cut() {
    System.out.println("Cutting the pizza in diagonal slices");
  }

  public void box() {
    System.out.println("Place pizza in official PizzaStore box");
  }
}
```

As you can see, we allow each type of pizza to be configured depending on the needs. 

```java
public class NYStyleCheesePizza extends Pizza {
  public NYStyleCheesePizza() {
    super(
      "NY Style Sauce and Cheese Pizza",
      "Thin crust dough",
      "Marinara Sauce",
      "Grated Reggiano Cheese"
    );
  }
}

public class ChicagoStyleCheesePizza extends Pizza {
  public ChicagoStyleCheesePizza() {
    super(
      "Chicago Style Deep Dish Cheese Pizza",
      "Extra Thick Crust Dough",
      "Plum Tomato Sauce",
      "Shredded Mozzarella Cheese"
    );
  }

  @Override public void cut() {
    System.out.println("Cutting the pizza into square slices");
  }
}
```

We can even override certain steps of the pizza preparation process to have different things occur.

### Meeting the parents... factory mom and factory dad!
You may not have noticed but we've already applied a type of factory pattern, the factory method pattern - where it 
encapsulates object creation by allowing subclasses decide what objects to create.

In our instance, we allow each `PizzaStore` subclass to decide what types of pizzas are created when the factory method
`createPizza()` is invoked. The `PizzaStore` is our creator whilst the `Pizza` is our product.

We use the factory pattern because we want to decouple the implementation of the product from its use since we don't 
want any changes to one to break the other.

An issue you might notice is that passing a String as input to our factory method might cause issues if we pass in the 
wrong input since a String can contain all sorts of nonsense. A simple solution to this is to simply contain all the 
available inputs as a set of constants using something like an enumeration.

```java
enum PizzaType {
  CHEESE, PEPPERONI
}

@Override Pizza createPizza(PizzaType type) { }
```

This makes our operations a lot more type safe and less prone to run time errors!

### Dependency under the scope
A dependency is introduced when we directly instantiate an object.

This continual idea of decoupling and reducing dependency between classes can be neatly phrased as

> Depend on abstractions. Do not depend on concrete classes.

This is also known as the **dependency inversion principle.** It states that not only should our high-level components
depend on abstraction, we should build our low-level components to depend on abstraction as well. A high-level component
refers to a class with behavior defined in terms of other low-level components.

If we look at the scenario proposed, if we did not apply the factory pattern, the `PizzaStore` (high level component) 
would depend on the concrete `Pizza` classes (low level component). Now that we applied the factory pattern, we only 
depend on the interface of `Pizza` rather than the concrete `Pizza` classes, and this applies to both the high-level
component and low-level component.

You might now be wondering where the idea of "inversion" came from in the name. Well, it stems from the idea of 
inverting one's understanding of OO design. When we apply the factor pattern, both the high-level component and 
low-level component has a dependence on the `Pizza` class, effectively inverting the top-to-bottom dependency chart to a
chart where the dependence points in 2 directions.

In order to stick to this pattern, there are several guidelines to follow:

* **No variable should hold a reference to a concrete class** - use a factory instead of `new`
* **No class should derive from a concrete class** - derive from an interface or abstract class
* **No method should override an implemented method of any of its base classes**

Do take note that whilst these are guidelines, following them 100% of the time will result in you never writing a single
program! Instead, take these as points to note as you write you programs, and if you violate one knowingly, you should
either have a good reason to do so or already have an idea of improving the current code to alleviate the violation.

### Ingredients 
We can further extend the factory pattern for our Pizza shop scenario. We can implement a factory for generating 
ingredients that the shop will use as each location might use something different.

```java
interface IngredientsFactory {
  Dough createDough();
  Sauce createSauce();
  Cheese createCheese();
  Veggies[] createVeggies();
  Pepperoni createPepperoni();
  Clam createClam();
}

class NYIngredients implements IngredientsFactory {
  @Override Dough createDough() {
    return new ThinCrustDough();
  }

  // ...
}
```

Then, we can begin reworking the `Pizza` class to account for these changes to the way ingredients are processed.

```java
abstract class Pizza {
  private String name;
  protected Dough dough;
  // ... all other ingredients

  abstract void prepare();

  // ...

  void setName(String name) {
    this.name = name;
  }
}
```

We now make `prepare()` abstract as we want to gather the ingredients from the `IngredientFactory`.

```java
class CheesePizza extends Pizza {
  private IngredientFactory ingredientFactory;

  CheesePizza(IngredientFactory ingredientFactory) {
    this.ingredientFactory = ingredientFactory;
  }

  @Override void prepare() {
    System.out.println("Preparing...");
    dough = ingredientFactory.createDough();
    // ...
  }
}
```

Notice how we retrieve the ingredients in the implementation of the `Pizza`. We also no longer create a class 
specifically for a location's pizza, instead, we make a generic `CheesePizza` and depend on the given 
`IngredientFactory` to specify what each location uses as ingredients.

```java
class NYPizzaStore extends PizzaStore {
  Pizza createPizza(String item) {
    Pizza pizza = null;
    IngredientFactory factory = new NYIngredients();

    if (item.equals("cheese")) {
      pizza = new CheesePizza(factory);
      pizza.setName("New York Style Veggie Pizza");
    }
  }
}

class ChicagoPizzaStore extends PizzaStore {
  Pizza createPizza(String item) {
    Pizza pizza = null;
    IngredientFactory factory = new ChicagoIngredients();

    if (item.equals("cheese")) {
      pizza = new CheesePizza(factory);
      pizza.setName("Chicago Square Cheese Pizza");
    }
  }
}
```

What we have done here is provide an interface for a family of products. This is also known as the 
`Abstract Factory Pattern`. This pattern allows the client to use an abstract interface to create a set of related 
products without knowing about the concrete products that are actually produced, effectively decoupling from any 
specifics of a concrete product.

In our scenario, the clients are the `PizzaStore`s that create the product (`Pizza`).

It is good to note that in the abstract factory pattern, we still use the factory method pattern.