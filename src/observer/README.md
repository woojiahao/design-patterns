# Observer Pattern
## Definition
> Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its 
> dependents are notified and updated automatically.

## Design principles
* [Strive for loosely coupled designs between objects that interact](https://github.com/woojiahao/design-patterns/tree/master/src/observer#loose-coupling)

## Scenario
We are building a weather station application and we have set up a system to receive atmospheric data when it changes.
This system is placed in the weather station. This is all behind a black box where we do not care about the 
implementation. What we do care about is receiving the updates and sending these updates to any connected weather 
display devices to show the changes.

From this basic description we have identified several actors of the application.
* **WeatherData** - This class will pull the data from the weather station and is responsible for notifying any 
  connected display devices
* **DisplayDevice** - This class will wait and listen for updates from the **WeatherData** and when updated, it will
  display the specific weather statistic when a change has been detected

A simplistic representation of how everything works can be seen here:

```java
interface DisplayDevice {
  void updateDisplay(float data);
}

class TemperatureDisplay implements DisplayDevice {
  @Override void updateDisplay(float data) {
    System.out.println("This just in, the temperature is " + data + " celsius");
  }
}

class HumidityDisplay implements DisplayDevice {
  @Override void updateDisplay(float data) {    
    System.out.println("This just in, the humidity is " + data + "%");
  }
}

class PressureDisplay implements DisplayDevice {
  @Override void updateDisplay(float data) {    
    System.out.println("This just in, the pressure is " + data + "kPa");
  }
}

class WeatherData {
  private DisplayDevice temperatureDisplay = new TemperatureDisplay();
  private DisplayDevice humidityDisplay = new HumidityDisplay();
  private DisplayDevice pressureDisplay = new PressureDisplay();

  public void measurementChange() {
    float temperature = getTemperature();
    float humidity = getHumidity();
    float pressure = getPressure();

    temperatureDisplay.updateDisplay(temperature);
    humidityDisplay.updateDisplay(humidity);
    pressureDisplay.updateDisplay(pressure);
  }
}
```

There are several flaws with this rendition of the code by itself and they are,

* We've coded to implementations since we've hard-coded all the display devices
* Whilst we have a common interface to use `updateDisplay()`, we have to call this method on every single display device 
  present and that should be encapsulated

Whilst the code is relatively simple, a big issue arises:

*"How do we get the WeatherData to trigger measurementChange and how do the display devices connected even listen for these changes?"*

### Naive solution
"Perhaps we could query the data every minute to see if it changes and have that push the update!" you might think.
Oh my sweet summer child, you are so naive. 

This approach is not scalable for a couple of reasons - having a system be occupied every minute for something like 
this might not be ideal if the system is always going to be engaging in some sort of computation as this can cause some 
delays, similarly, if the weather station decides to install a hyper-sensitive equipment that pushes updates every 
second, or worse, every millisecond, will we now query the weather station every whatever miniscule unit of time and 
hog up all the power?

### Newspaper rounds...
Let's first adjust our understanding to fit what the observer pattern employs. To understand this, we can think of the
observer pattern like a newspaper delivery service. In a delivery service, there are 2 parties involved and the general
interaction goes as such:

#### Parties
1. Newspaper service
2. Reader

#### Steps involved
1. The newspaper service announces that it is open for business
2. A reader who wants to receive the newspaper daily will then indicate interest, effectively "subscribing" to the
   newspaper service
3. Everyday, the newspaper service will send out a delivery boy to go to each of the subscribed readers' home to deliver
   the newspaper

In this interaction, if the reader wishes to opt out of the service, they can always do so at any time and the newspaper
service will not have to make a big change to their delivery system - all they need to do is remove this reader and 
they can continue delivering newspaper to the rest of their customers without worry.

At any point in time, this single newspaper service can have multiple subscribers. This is where the `one-to-many` 
relationship part of the observer pattern comes into play.

### Observer pattern under the microscope
The newspaper example clearly described everything the observer pattern is about. In the observer pattern, we first have
a thing that users can subscribe to, or in this context, they are able observe. In the observer pattern, we call this
entity the subject. In the newspaper example, this was the **newspaper service** and in the weather station example,
this would be the `WeatherData` object. 

```java
interface ISubject {
  void addObserver(IObserver observer);
  void removeObserver(IObserver observer);
  void notifyObservers();
}
```

Then, we would need to provide the observers with an interface to listen to the updates, which as you can see, we've 
already drafted a sample in the `ISubject` class, and that is the `IObserver` class. This is the subscriber of the 
newsletters in the newspaper example and it is the `DisplayDevice`s that are listening into the changes of 
`WeatherData`.

```java
interface IObserver {
  void update();
}
```

Now, we can hook in observers to a subject and have the subject publish events that the observers will be notified of.

But what we've just added is simply the interface design, we do this because we want to provide some baseline of what 
all subjects and observers should look like. It's an interface so that we can leverage Java's ability for 
multi-interfaces. In actuality, the implementation will have to include several other key pieces:

```java
class Subject implements ISubject {
  private List<IObserver> observers = new ArrayList<>();

  @Override void addObserver(IObserver observer) {
    observers.add(observer);
  }

  @Override void removeObserver(IObserver observer) {
    observers.remove(observer);
  }

  @Override void notifyObservers() {
    observers.forEach(Observer::update);
  }
}

class Observer implements IObserver {
  private String name;

  Observer(String name) {
    this.name = name;
  }

  @Override void update() {
    System.out.println(name + " update received!");
  }
}
```

Notice how we've added a `List<Observer> observers` to the `Subject` implementation? We have to do so since we need to 
store the observers somewhere. Now, we've got a rudimentary observer pattern going, where we can use it as such:

```java
ISubject subject = new Subject();
IObserver observer1 = new Observer("Tim");
subject.addObserver(observer1);
subject.notifyObservers();

System.out.println("\n");

IObserver observer2 = new Observer("Jane");
subject.addObserver(observer2);
subject.notifyObservers();

> Tim update received!
>
> Tim update received!
> Jane update received!
```

### Loose... coupling!
So why did we even consider the observer pattern? Other than it being incredibly suitable for our use case, it pushes
the principle of **loose coupling.**

> Strive for loosely coupled designs between objects that interact

So what exactly is this "loose coupling" we are striving for, well it's this idea that any related classes should not 
be so tied to one another that changing one will break the other - aka, these classes should know enough about one 
another to do their job, but not enough to screw one another over if they change.

Let's inspect how we uphold this principle with the observer pattern.

* By using an interface to describe `IObserver` we've made it so the `ISubject` knows nothing about the observer's true
  implementation, all it knows is that the observer will implement the `IObserver` interface and in doing so, it's not 
  bothered with the implementation
* Adding/removing an `IObserver` is simple since all the `ISubject` holds is a list of `IObserver`s and this set of 
  operations can always be done during runtime
* If we create a new `Observer` type, we do not need to modify the `Subject` since like point 1, the `Subject` knows
  nothing about the implementation of the `Observer`, all it knows is that it implements the `IObserver` interface

  ```java
  class SpecialObserver implements IObserver {
    @Override void update() {
      System.out.println("Special observer notified!")
    }
  }
  ```

  `SpecialObserver` can be added to the `Subject` with ease.

  ```java
  IObserver special = new SpecialObserver();
  subject.addObserver(special);

  > Tim update received!
  > Jane update received!
  > Special observer notified!
  ```

* To play upon my initial point, changing the implementation of the `Subject` or `Observer` would not really influence
  the implementation of the other since they both only know the bare minimum to operate with one another
* Lastly, because these classes are built from interfaces, they're reusable!

### Building out the weather system
Now, let's look at how we can integrate the weather system with the newly learnt observer pattern!

First, we should identify who our `Subject` is and in this scenario, we know that since the `WeatherData` is going to be
observed by others, it is our `Subject`.

```java
interface ISubject {
  void addObserver(IObserver observer);
  void removeObserver(IObserver observer);
  void notifyObserver();
}

class WeatherData implements ISubject {
  private List<IObserver> observers = new ArrayList<>();
  private float temperature;
  private float humidity;
  private float pressure;

  @Override void addObserver(IObserver observer) {
    observers.add(observer);
  }

  @Override void removeObserver(IObserver observer) {
    int i = observers.indexOf(observer);
    if (i >= 0) {
      observers.remove(observer);
    }
  }

  @Override void notifyObservers() {
    observers.forEach(observer -> observer.update(temperature, humidity, pressure));
  }

  public void setMeasurements(float temperature, float humidity, float pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;
    notifyObservers();
  }
}
```

Notice how we designed the `IObserver` (even though we haven't created the code for it) to have the `update` method 
include input for the temperature, humidity and pressure? Well we can do so because this method is meant to be the
Observer's only connection to the subject and as such, we should pass as much information to it as possible so that it 
is able to use the information from the subject as needed

Now, let's implement the `IObserver`s, which are the display devices.

```java
interface DisplayDevice {
  void updateDisplay();
}

interface IObserver {
  void update(float temperature, float humidity, float pressure);
}

class CurrentSummaryDevice implements DisplayDevice, IObserver {
  private float temperature;
  private float humidity;
  private float pressure;
  private ISubject subject;

  CurrentSummaryDevice(ISubject subject) {
    this.subject = subject;
    subject.addObserver(this);
  }

  @Override void update(float temperature, float humidity, float pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;
  }

  @Override void updateDisplay() {
    System.out.println(""); // Whatever formatting to display everything
  }
}
```

Note that we were able to implement both the `DisplayDevice` and `IObserver` interfaces in our `CurrentSummaryDevice` 
and this is thanks to Java's allowance of multiple inheritances from interfaces. If we had used an abstract class 
instead, we would not be able to inherit both at once and that would restrict our ability to design classes - hence
highlighting the point of programming to interfaces, not implementations.

Cool! We've now adopted a basic Observer pattern that allows for **pushing** of data, let's look at how the Java API 
allows users to **pull** data!

### Built-in observer pattern
Java offers a built-in observer pattern in the packages: `java.util.Observer` and `java.util.Observable`. Let's look at 
a simple implementation of `Observable` and we'll dissect what we see.

```java
abstract class Observable {
  abstract void addObserver(Observer observer);
  abstract void deleteObserver(Observer observer);
  abstract void notifyObservers();
  abstract void setChanged();
}

interface Observer {
  void update(Observable o, Object arg);
}
```

Right off the bat, the biggest thing you'll notice is that `Observable` is a class, not an interface, that means we're
unable to extend another class if we already extend the `Observable` class. This is a big flaw with the design of the 
built-in observer pattern. If this limits your application, it is best to roll your own version of the observer pattern.

#### setChanged() ?
Another glaring difference between our implementation of `ISubject` and `Observable` is this `setChanged()` method. Why
exactly do we need this? To put simply, this method signifies that the state has changed and that `notifyObservers()`,
when called, should notify all observer only if the state is valid.

```java
void setChanged() {
  changed = true;
}

void notifyObservers() {
  if (changed) {
    observers.forEach(Observer::update);
    changed = false;
  }
}
```

This serves as a valve, determining when we allow `notifyObservers` to actually send the notification and when to stop
letting it.

#### Pushing vs pulling
As discussed earlier, our implementation of the observer pattern focussed on the idea of the subject **pushing** 
information to the observers and the observers just sit there receiving the information when necessary. Whilst this is
good, if the subject contains a lot of information, it becomes less ideal for the observer to be fed with a large number
of arguments - can you imagine the size of the parameter list? Insane.

This is where the idea of **pulling** comes into play, where the observers are the one who retrieve the necessary 
information from the subject. In the Java API's implementation of `Observer` you will notice that the `update()` method
receives 2 inputs, 1 for the `Observable` that notified it and the other for an `Object` to represent any set of 
information to be pushed to the observer.

By passing both arguments to the `update()` method, it allows for both pushing and pulling to be performed. If we have
information to push to the observer, we can pass it to the `args` parameter and have the observer cast the information 
if necessary.

```java
class Foo implements Observer {
  @Override void update(Observable o, Object args) {
    if (args instanceof Integer) {
      // do something with args 
    }
  }
}
```

On the other hand, we can also pass the Observable to `o` and have the observer extract the information from the 
Observer by casting it - effectively letting the observer pull this information.

```java
class Subject extends Observable {
  public String name;

  @Override notifyObservers() {
    observers.forEach(observer -> observer.update(this, null));
  }
}

class Foo implements Observer {
  @Override void update(Observable o, Object args) {
    if (o instanceof Subject) {
      System.out.println("Subject's name " + ((Subject) o).name);
    }
  }
}
```