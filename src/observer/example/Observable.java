package observer.example;

import java.util.ArrayList;
import java.util.List;

public class Observable implements IObservable {
  private List<IObserver> observers = new ArrayList<>();

  @Override public void addObserver(IObserver observer) {
    observers.add(observer);
  }

  @Override public void removeObserver(IObserver observer) {
    observers.remove(observer);
  }

  @Override public void notifyObservers() {
    observers.forEach(IObserver::notifyObserver);
  }
}
