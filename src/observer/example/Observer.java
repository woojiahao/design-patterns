package observer.example;

public class Observer implements IObserver {
  @Override public void notifyObserver() {
    System.out.println("Notified");
  }
}
