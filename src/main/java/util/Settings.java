package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

  private static final String configFileName = "config.properties";
  private static Properties properties;

  static {
    InputStream input = Settings.class.getClassLoader().getResourceAsStream(configFileName);
    if (input == null) {
      throw new RuntimeException("Unable to find file: " + configFileName);
    }

    properties = new Properties();

    try {
      properties.load(input);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static String getWeatherApiKey() {
    return (String) properties.get("weather-api-key");
  }

  public static String getGeoLocationApiKey() {
    return (String) properties.get("geolocation-api-key");
  }

  public static int getPort() {
    return Integer.parseInt((String) properties.get("port"));
  }

  public static int getNThreads() {
    return Integer.parseInt((String) properties.get("threads"));
  }
}
