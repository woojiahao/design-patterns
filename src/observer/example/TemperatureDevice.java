package observer.example;

class TemperatureDevice implements DisplayDevice, IObserver {
  private ISubject weatherData;
  private float previousTemperature;
  private float newTemperature;

  TemperatureDevice(ISubject weatherData) {
    this.weatherData = weatherData;
    weatherData.addObserver(this);
  }

  @Override public void updateDisplay() {
    System.out.println(String.format(
      "The previous temperature was %s celsius, it is now %s celsius, an %s change",
      previousTemperature,
      newTemperature,
      (newTemperature - previousTemperature)
    ));
  }

  @Override public void update(float temperature, float humidity, float pressure) {
    previousTemperature = newTemperature;
    newTemperature = temperature;
    updateDisplay();
  }
}
