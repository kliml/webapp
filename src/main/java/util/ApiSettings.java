package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiSettings {

  private static final String configFileName = "config.properties";
  private static Properties properties;

  static {
    InputStream input = ApiSettings.class.getClassLoader().getResourceAsStream(configFileName);
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
}
