package observer.example;

public class Main {
  public static void main(String[] args) {
    WeatherData data = new WeatherData();
    IObserver summaryDevice = new CurrentSummaryDevice(data);
    data.setMeasurements(14.0f, 30.0f, 3.0f);
    IObserver temperatureDevice = new TemperatureDevice(data);
    data.setMeasurements(20.0f, 45.0f, 15.0f);
    data.setMeasurements(23.0f, 50.0f, 69.0f);
  }
}
