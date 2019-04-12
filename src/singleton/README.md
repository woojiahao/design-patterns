# Singleton Pattern
## Definition
> Singleton pattern ensures a class has only one instance, and provides a global point of access of it.

## Design principles
None 😢

## Scenario
The concept of the singleton pattern is to assist with the creation of a **single** object. Whilst this may seem 
strange, there is in fact a lot of use cases for such a pattern. For instance, thread pools, caches, dialogue boxes,
device drivers and loggers - if these objects were instantiated more than once, we might run into issues like incorrect
behavior and the overuse of resources.

The first instinct when thinking about a solution to this problem would be to create the object instance as static and 
it will automatically be created when the application begins. Whilst there is merit to this idea, if the object that's 
created is going to be computationally intensive the moment it's instantiated, your application will slow down the 
moment it's made, which isn't ideal if we're particular about memory management and performance.

In order to counter that particular disadvantage, the singleton pattern is designed to create objects when needed.

### Basic singletons
Let's first create a basic Singleton pattern.

```java
class Singleton {
  private static Singleton instance;

  private Singleton() {

  }

  public static Singleton getInstance() {
    if (instance == null) {
      instance = new Singleton();
    } 
    return instance;
  }
}
```

We make use of a key piece of behavior classes have - private constructors. Making a constructor private will ensure 
that no one from outside the class will be able to create an instance of the class. This means that 
`Singleton singleton = new Singleton();` isn't allowed.

We store a static instance of the singleton object in our class and use the `getInstance()` method to retrieve this
static instance. Instead of exposing the variable as public, we use a method because we have several pieces of behavior 
we wish the singleton to have.

```java
public static Singleton getInstance() {
  if (instance == null) {
    instance = new Singleton();
  }

  return instance;
}
```

Dissecting the `getInstance()` method, you'll discover that rather than instantiating the private `instance` 
immediately, we only instantiate it the first time it's called using `instance == null` to check if it's the first time
the singleton is being used. Then, we simply return the instance.

Now, let's dive into a more interesting scenario.

### Chocolate factory
We're writing code for a chocolate factory and they want to create a program to manage their chocolate boiler. A basic
class would look like:

```java
class ChocolateBoiler {
  private boolean empty = true;
  private boolean boiled = false;

  public void fill() {
    if (empty) {
      empty = false;
      boiled = false;
      // fill boiler with milk/chocolate mixture
    }
  }

  public void drain() {
    if (!empty && boiled) {
      empty = true;
    }
  }

  public void boil() {
    if (!empty && !boiled) {
      boiled = true;
    }
  }

  public boolean isEmpty() {
    return empty;
  }

  public boolean isBoiled() {
    return boiled;
  }
}
```

The chocolate factory only has 1 chocolate boiler so having too many would be overkill and cause loads of problems. 
This is where our singleton pattern can come into play.

### Singleton choco boilers!
Let's start by defining the `ChocolateBoiler` as a singleton.

```java
class ChocolateBoiler {
  private static ChocolateBoiler instance;

  private boolean empty = true;
  private boolean boiled = false;

  private ChocolateBoiler() {}

  public static ChocolateBoiler getInstance() {
    if (instance == null) {
      instance = new ChocolateBoiler();
    }

    return instance;
  }

  // ... 
}
```

Pretty cool stuff!!

### Oh no! The threads are here...
> Long ago, the ~~four nations~~ chocolate boilers were in harmony, but then, everything thing changed when the 
> ~~fire nation~~ intern used threads

Whilst the singleton we've designed returns only a single instance of the class, we're not protected from threads and 
race cases.

Imagine, a thread accesses the `ChocolateBoiler` to `fill()` and another accesses the `ChocolateBoiler` at the same
time to `boil()`, something that shouldn't occur. This can cause major issues and cause the `empty` and `boiled` states
to be messed up.

At the same time, if 2 threads call `getInstance()` for the first time, `ChocolateBoiler` will be initialised twice, 
causing the instance used by thread 1 to be different from the one used by thread 2. Similarly, the thread that leaves
last, will override the instance causing inconsistencies.

Therefore, we have to make the code thread safe. 

### Thread safe singletons
In order to ensure that only 1 thread can invoke the `getInstance()` method at any point in time, we must make the 
method a `synchronized` method.

```java
public static synchronized getInstance() {
  // ...
}
```

`synchronized` ensures that no 2 threads can enter the method at the same time. Whilst synchronization might add slight
overhead when accessing the method, this is only the first time `getInstance()` is ever called because once the method
is called before, `instance` will be initialized and the order of access will no longer matter.   

Awesome! We resolved the threading issue with our code. Whilst this might resolve the issue, making a method 
synchronized still adds overhead to the method and if there's a 100 threads (not recommended at all) calling the 
`getInstance()` method, there might be some delay, especially if the method does more than just the basic null checking
and returning. So what are some alternative solutions to this?

### Alternative singletons...
#### Eagerly created instance
If the application is continuously accessing the singleton instance, it might be easier to create the singleton eagerly.

```java
class Singleton {
  private static Singleton instance = new Singleton();

  private Singleton() { }

  private static Singleton getInstance() {
    return instance;
  }
}
```

Using this approach, the JVM creates an instance of the singleton when the class is loaded. If multiple threads access
the singleton, the same instance is returned regardless and there is no multi-threading issues, thus alleviating the 
overhead of using the `synchronized` keyword.

#### Double-checked locking
This approach uses the `synchronized` block over the `synchronized` method modifier to ensure that multiple threads 
don't create different instances of the singleton and it avoids the continual overhead of synchronization if the 
`getInstance()` method is being called subsequent times.

```java
class Singleton {
  private volatile static Singleton instance;

  private Singleton() { }

  private static Singleton getInstance() {
    if (instance == null) {
      synchronized(Singleton.class) {
        if (instance == null) {
          instance = new Singleton();
        }
      }
    }
  }

  return instance;
}
```