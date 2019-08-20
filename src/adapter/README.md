# Adapter Pattern
## Description
> Adapter pattern converts the interface of a class into another interface the clients expect. Adapter lets classes 
> work together that couldn't otherwise because of incompatible interfaces.

## Design patterns
None 😢

## Scenario
The adapter pattern, much like the its name entails, is a design pattern written to link 2 components of a system 
together that otherwise do not fit - much like a power adapter. For instance, you have just replaced your backend to use
a new vendor's library. However, your original system was designed to work with a specific vendor and now that 
everything has changed, you have to adopt the new vendor system.

Whilst a straightforward solution to this problem would be to refactor the entire codebase to use the new vendor's API,
the maintenance cost of doing so is not a worthwhile tradeoff. Smaller projects might not suffer from the same downsides
of a massive refactor job that much larger and more elaborate solutions might encounter.

The next best solution is to build a class that adapts the vendor interface into the one that your original program was 
expecting. This adapter acts as a middleman by receiving requests from the client and converting them into requests 
that makes sense on the vendor class.

### Turkeys!
Let's take a look at the first example used for this design pattern journey - `Duck`s! Imagine that we have developed 
a system that depends on the `Duck` interface - and its subclasses, to supply information. However, after revisiting 
the functional requirements, it turns out the we want to supply turkeys if there are not enough ducks. But we've 
already created our system to be tightly coupled with the interface of `Duck`.

```java
interface Duck {
  void quack();
  void fly();
}
```

The `Turkey` interface:

```java
interface Turkey {
  void gobble();
  void fly();
}
```

A direct approach to resolving this is to create something like a backing `Bird` class and have each interface subclass
it. However, this is not maintainable and might result in class explosions out of our reach.

Instead, in order to allow our existing system to work with `Turkey` as though it was a `Duck`, we create an adapter 
that implements the `Duck` interface so that pre-existing code doesn't have to be changed.

```java
class TurkeyAdapter implements Duck {
  Turkey turkey;

  TurkeyAdapter(Turkey turkey) {
    this.turkey = turkey;
  }

  @Override void quack() {
    turkey.gobble();
  }

  @Override void fly() {
    turkey.fly();
  }
}
```

Now with this brand spanking new `TurkeyAdapter`, we can now use any existing systems that relied on the `Duck` 
interface.

```java
Duck mallard = new MallardDuck();
Turkey wild = new WildTurkey();
Duck turkeyDuck = new TurkeyAdapter(wild);
turkeyDuck.quack();
```

From the `TurkeyAdapter` example, we can see that the adapter will implement the target interface (`Duck`) and hold an
instance of the Adaptee (`Turkey`).

The adapter translates the generic request into one or more calls to the adaptee and becomes the middleware of any 
system request.

The benefit of using an adapter is that we decouple to end client from the implemented interface. This a strong point 
because if we expect the implementation to change, we don't want to have to fumble with so many different changes and
end up with a refactoring hell hole. Instead, we want the client to use an encapsulated version of the implemented 
interface, allowing us to avoid any future potential headaches.

### Object and class adapters... what what?!
A **class adapter** will inherit from both the target and adaptee - using inheritance over composition, to allow the 
client to interface with the adapter with either the API of the target or adaptee. This not only violates one of the 
fundamental rules of OOP design, it's also not possible with Java as multiple inheritance is not present in Java.

The example we've gone through earlier is a case of an **object adapter** in use. The difference between 
*class adapters* and *object adapters* is the use of composition vs inheritance.

### Adapter pattern irl!
Now, let's take a look a real life application of the adapter pattern in Java's library. In the past, an `Enumeration`
interface existed, allowing users to go through a collection (`Stack`, `Vector` and `Hashtable`). It had a signature 
like...

```java
interface Enumeration<T> {
  boolean hasMoreElements();
  T nextElements();
}
```

This existed in the older version of the collection library. Java received a major revamp to their collections 
framework, and along came a new way to iterate over collections - `Iterator`

```java
interface Iterator<T> {
  boolean hasNext();
  T next();
  void remove();
}
```

This is where the adapter pattern comes in handy, we need a way to interface with code that originally relied on 
`Enumeration` but through `Iterator`. This is where `EnumerationIterator` comes into play. It acts as an adapter to link
the target (`Iterator`) to the adaptee (`Enumeration`).

Because `Iterator` supports the method `remove()` - something `Enumeration` doesn't, as such, in order to ensure 
backward compatibility with our original code, we have to throw an `UnsupportedOperationException` if the user decides
to use the `remove()` method of the `EnumerationIterator`. 