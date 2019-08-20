package observer.example;

import java.util.ArrayList;
import java.util.List;

class WeatherData implements ISubject {
  private List<IObserver> observers = new ArrayList<>();
  private float temperature;
  private float humidity;
  private float pressure;

  @Override public void addObserver(IObserver observer) {
    observers.add(observer);
  }

  @Override public void removeObserver(IObserver observer) {
    if (observers.indexOf(observer) != -1)
      observers.remove(observer);
  }

  @Override public void notifyObservers() {
    observers.forEach(observer -> observer.update(temperature, humidity, pressure));
  }

  void setMeasurements(float temperature, float humidity, float pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;
    notifyObservers();
  }
}
