# Facade Pattern
## Description
> Facade pattern provides a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level
> interface that makes the subsystem easier to use.

## Design patterns


## Scenario
We've just bought a new home and it comes with a theatre system! However, the system contains many sub-components that 
we have to manage before we can fully utilise the system in place. Say we want to have a preset where we want to watch
movies, so we would:

- Turn on the projecter 
- Dim the lights
- Up the bass on the audio system
- Put on the show to watch
- Adjust the volume
- etc...

For instance, we might have code similar to this:

```java
class Light {
  public void on() { }
}

class Projecter {
  public void on() { }
}

class Speakers {
  public void increaseBass() { }
  public void adjustVolume() { }
}
```

As you would notice, there are many steps involved if you wish to create a preset and performing this manually will be 
too hard to keep track of. So instead, we should employ an approach that will reduce the overhead and provide a single 
entry to enable the preset, such as **Movie Mode** in our theatre system and have it perform all of those minute 
subtasks for us. 

This is what the facade pattern is in essence, it provides a simplified interface to hide any complexity behind a facade
and provides the end-user with a much cleaner experience when using your programs.

The facade pattern also faciliates ease of change - what if you wish to use the same preset over and over again in your
code, if you do not create a facade for it, you'll have to duplicate the messy logic multiple times across different 
files and if you make a change to the fundamental logic of the preset, you'll have to remember to edit every other place
otherwise you will create inconsistencies. With the facade pattern, you are programming to an interface, meaning that if
you create the facade for the preset, the only location you have to change details of the preset is the interface 
created. Any place where the preset facade is used will have the changes take effect and this reduces the overhead for 
change. This split decouples the client and the backend.

```java
class TheatreFacade {
  Light light;
  Speakers speakers;
  Projecter projecter;

  void startMovie() {
    light.on();
    speakers.increaseBass();
    speakers.adjustVolume();
    projecter.on();
  }
}

TheatreFacade theatre = new TheatreFacade();
theatre.startMovie();
```

A big catch to the facade pattern is that the sub-components are not removed from your program. They are still available
to be used individually, the facade just provides a means to collate these components together to perform a joint 
action.

Another benefit the facade patterns brings to the table is the idea of extensibility - the facade is free to add 
additional behavior to the system as it sees fit.

### Adapter vs facade
The big differentiator between the adapter pattern and the facade pattern is the that the adapter pattern is created to 
change the interface of one class to another so that it matches what the client wants. On the other hand, the facade 
pattern simply creates an interface for the sub-components and it doesn't attempt to change the interface of a class to
conform to a standard.

### Principle of least knowledge 
> Talk only to your immediate friends 

This principles