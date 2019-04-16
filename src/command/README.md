# Command Pattern
## Description
> Command pattern encapsulates a request as an object, thereby letting you parameterize other objects with different 
> requests, queue or log requests, and support undoable operations. 

## Design principles
None 😢

## Scenario
We have received a request to program a remote control that manages multiple devices at once. We have to implement the 
on/off functionality of the remote control as well as an undo button so that we will be able to undo the latest 
operation we performed.

Some of the devices we are to implement the remote controller include...

```java
class OutdoorLight {
  void on() { }
  void off() { }
}

class TV {
  void on() { }
  void off() { }
  void setInputChannel() { }
  void setVolume() { }
}

class FaucetControl {
  void openValve() { }
  void closeValve() { }
}
```

As you can see the system isn't as straight-forward as one might expect. Each device has a slightly different API to 
interact with it and it would be impossible to factor in all possible devices without making the remote control tightly
coupled to each device.

What we want is to decouple the remote control from the devices. The remote control will simply hold a reference to some
object that represents a command and it will invoke the action of the command without thinking about the implementation 
of the command object.

### Diner experience
Let's examine a basic flow that a diner will employ to serve their customers.

The customer will first order the food that they want (`createOrder()`), which prompts the server to take the order 
from the customer (`takeOrder()`). The server will then pass the order to the kitchen (`orderUp()`). The kitchen 
receives the order, prepares the order (`makeBurger()`, `makeShake()`) and once done, they will serve the prepared food
to the customer.

Observing this interaction, we notice that the kitchen doesn't need to be told every single item to prepare by the 
server, instead, the server will pass them a order receipt, which contains the request to prepare the meal. If we think 
of this order receipt like an object, you'll soon notice that it has an interface comprising of a single method,
`orderUp()`. This method encapsulates the actions required to prepare the meal. It also holds a reference to the person
in charge of preparing the meal, aka the kitchen. The server doesn't anything about the order, rather, all she does it
take the order receipt from the customer and passes it off to the order window. 

This way, the server is decoupled from the kitchen. The server knows nothing about the meal nor the preparation methods
employed, and the kitchen knows nothing about the server. They each perform their separate jobs and the only means of
communication between them is the order receipt. The server doesn't talk to the kitchen and vice versa. A perfect cog 
in the machine!

### Putting the pieces together...
Think of the remote control in terms of the diner. We start with a client, that stores a command and passes the command
to the invoker which will execute the command stored. The command we pass about stores the actions and the receiver of 
the actions.

For the command pattern to take shape, we first need to design the `Command` that we pass about.

```java
interface Command {
  public void execute();
}
```

Each command has a unique implementation, for instance, let's attempt to implement the command for a light.

```java
class LightCommand implements Command {
  Light light;

  public LightCommand(Light light) {
    this.light = light;
  }

  @Override public void execute() {
    light.on();
  }
}
```

The `LightCommand` object has all the we are looking for, we have a standard method to invoke in `execute()` and in 
each implementation of the `Command`, it has a reference to the object to perform some actions when the standard method
of `Command` is invoked.

After designing the command, we now design the remote control.

```java
class SimpleRemoteControl {
  Command slot;

  public void setCommand(Command command) {
    slot = command;
  }

  public void buttonWasPressed() {
    slot.execute();
  }
}
```

A sample usage of the code looks like:

```java
SimpleRemoteControl control = new SimpleRemoteControl();
Light light = new Light();
LightCommand lightOn = new LightCommand(light);

remote.setCommand(lightOn);
remote.buttonWasPressed();
```

### Inspecting the command pattern
Whilst it was not explicitly stated in the simple remote control, there is actually 5 different moving parts of our 
command pattern.

- `Client` - responsible for creating a `ConcreteCommand` and setting its `Receiver` ie. `main()`
- `Receiver` - knows how to perform the work needed to carry out the request ie. `Light` 
- `ConcreteCommand` - defines binding between an action and `Receiver`, the `Invoker` makes a request by calling 
  `execute()` ie. `LightCommand`
- `Command` - interface for all commands ie. `Command` 
- `Invoker` - holds a command and can at some point carry out a request ie. `SimpleRemoteControl`

### Adding multiple commands
Once we've gotten the basic foundations of the command pattern down, we can begin to implement the remote controller's 
full functionality. First off, the remote controller should house more than just 1 command. We can store multiple 
commands using an array.

```java
class RemoteControl {
  Command[] onCommands = new Command[7];
  Command[] offCommands = new Command[7];

  public void setCommand(int slot, Command onCommand, Command offCommand) {
    onCommands[slot] = onCommand;
    offCommands[slot] = offCommand;
  }

  public void onButtonWasPushed(int slot) {
    onCommands[slot].execute();
  }

  public void offButtonWasPushed(int slot) {
    offCommands[slot].execute();
  }
}
```

We store both the commands for when the on button is pressed and for when the off button is pressed. Now, we can store
multiple commands in the remote control. 

```java
class StereoOnWithCDCommand implements Command {
  Stereo stereo;

  StereoOnWithCDCommand(Stereo stereo) {
    this.stereo = stereo;
  }

  @Override public void execute() {
    stereo.on();
    stereo.setCD();
    stereo.setVolume(11);
  }
}
```

So this is a sample use of the new `RemoteControl`.

```java
RemoteControl remoteControl = new RemoteControl();

Light livingRoomLight = new Light("Living room");
Light kitchenLight = new Light("Kitchen");
CeilingFan ceilingFan = new CeilingFan("Living room");
GarageDoor garageDoor = new GarageDoor();
Stereo stereo = new Stereo("Living room");

LightOnCommand livingRoomLightOn = new LightOnCommand(livingRoomLight);
LightOffCommand livingRoomLightOff = new LightOffCommand(livingRoomLight);
LightOnCommand kitchenLightOn = new LightOnCommand(kitchenLight);
LightOffCommand kitchenLightOff = new LightOffCommand(kitchenLight);

CeilingFanOnCommand ceilingFanOn = new CeilingFanOnCommand(ceilingFan);
CeilingFanOffCommand ceilingFanOff = new CeilingFanOffCommand(ceilingFan);

GarageDoorUpCommand garageDoorUp = new GarageDoorUpCommand(garage);
GarageDoorDownCommand garageDoorDown = new GarageDoorDownCommand(garage);

StereoOnWithCDCommand stereoOnWithCD = new StereoOnWithCDCommand(stereo);
StereoOffWithCDCommand stereoOffWithCD = new StereoOffWithCDCommand(stereo);

remoteControl.setCommand(0, livingRoomLightOn, livingRoomLightOff);
remoteControl.setCommand(1, kitchenLightOn, kitchenLightOff);
remoteControl.setCommand(2, ceilingFanOn, ceilingFanOff);
remoteControl.setCommand(3, garageDoorUp, stereoOffWithCD);
remoteControl.setCommand(4, stereoOnWithCD, stereoOffWithCD);

remoteControl.onButtonWasPushed(0);
remoteControl.offButtonWasPushed(0);

remoteControl.onButtonWasPushed(1);
remoteControl.offButtonWasPushed(1);

remoteControl.onButtonWasPushed(2);
remoteControl.offButtonWasPushed(2);

remoteControl.onButtonWasPushed(3);
remoteControl.offButtonWasPushed(3);

remoteControl.onButtonWasPushed(4);
remoteControl.offButtonWasPushed(4);
```

You might notice that if you try to access commands outside of the ones we set, they will throw a NPE. This is because 
if we don't supply a command, it gets defaulted to `null` where if we attempt to call `execute()` on it, it will throw 
the NPE exception. We have 2 alternatives to get around that.

#### Null checking
The first solution is the conventional null checking of the `command[slot]`. This, however, adds code duplication since
the same null checking has to be implemented with the `offCommands`.

```java
public void onButtonWasPushed(int slot) {
  Command command = onCommands[slot];
  if (command != null) {
    command.execute();
  }
}
```

#### Null objects
Instead, we can offload the need for null checking by creating a null object.

```java
class NoCommand implements Command {
  public void execute() { }
}
```

Then during the initialization of the `RemoteControl`, we store the default commands as `NoCommand`.

```java
RemoteControl() {
  for (int i = 0; i < 7; i++) {
    onCommands[i] = new NoCommand();
    offCommands[i] = new OffCommand();
  }
}
```

This way, if the current slot houses a `NoCommand`, it does nothing and you avoid the NPE and null checking all 
together.

### Undo!
We've already built the base for our command pattern. The last thing we need to implement is the idea of an undo button - 
allowing us to undo the most recent operation performed.

The first thing to do is to update the `Command` interface to include a method for `undo()`.

```java
interface Command {
  public void execute();
  public void undo();
}
```

Then, we change the implementation of each `Command` to meet the changes.

```java
class LightOnCommand implements Command {
  // ...
  @Override public void undo() {
    light.off();
  }
}
```

Then, we want the `RemoteControl` to store the most recent `Command`.

```java
class RemoteControl {
  // ...
  Command recentCommand = new NoCommand();

  // ...

  public void onButtonWasPushed(int slot) {
    Command command = onCommands[slot];
    command.execute();
    recentCommand = command;
  }

  public void offButtonWasPushed(int slot) {
    Command command = offCommands[slot];
    command.execute();
    recentCommand = command;
  }

  public void undoButtonWasPushed() {
    recentCommand.undo();
  }
}
```

### Simplifying code
A pitfall of the command pattern is the class explosion that can be caused by declaring commands for everything. This 
can cause maintenance hell and be incredibly hard to keep track of everything. As such, we can make use of lambdas to 
reduce the problems we may encounter.

Note that we will be using the `Command` interface as a functional interface, and as such, we do not include the 
`undo()` method.

```java
interface Command {
  void execute();
}
```

As with any functional interface, we can create a lambda for it.

```java
Light light = new Light("Lambdas!");
remoteControl.setCommand(0, () -> { light.on(); }, () -> { light.off(); });
```

We can further simplify this code by using method references.

```java
remoteControl.setCommand(0, light::on, light::off)
```

Using lambdas significantly reduce our class footprint and enable us to create much shorter and concise code.

### Using commands to queue requests
Since a command stores an action, we can add those to a queue and use them similar to a message queue where the receiver 
will look through and execute in order.

### Using commands for logging requests
We can use commands to store and load the state of an operation. In the event where the operation fails, we are able to 
restore a prior copy of the `Receiver` without losing the data.