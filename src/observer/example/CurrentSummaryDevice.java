package observer.example;

class CurrentSummaryDevice implements DisplayDevice, IObserver {
  private ISubject weatherData;
  private float temperature;
  private float humidity;
  private float pressure;

  CurrentSummaryDevice(ISubject weatherData) {
    this.weatherData = weatherData;
    weatherData.addObserver(this);
  }

  @Override public void updateDisplay() {
    System.out.println(
      String.format(
        "The current statistics: %s celsius, %s humid and %skPa",
        temperature,
        humidity,
        pressure
      )
    );
  }

  @Override public void update(float temperature, float humidity, float pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;

    updateDisplay();
  }
}
