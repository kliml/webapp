package servlet;

import util.Settings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherServlet extends HttpServlet {

  private static final String weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s";
  private static String apiKey;

  @Override
  public void init() throws ServletException {
    super.init();
    System.err.println(this.toString() + " init");
    apiKey = Settings.getWeatherApiKey();
  }

  @Override
  public void destroy() {
    super.destroy();
    System.err.println(this.toString() + " destroy");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String cityCode = req.getParameter("city");

    if (cityCode == null) {
      //TODO
    }

    URL apiUrl = new URL(String.format(weatherApiUrl, cityCode, apiKey));
    HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
    apiConnection.setRequestMethod("GET");

    apiConnection.connect();

    int responseCode = apiConnection.getResponseCode();
    System.err.println(responseCode);

    if (responseCode == 200) {
      InputStream inputStream = apiConnection.getInputStream();
      BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      reader.close();
      String body = sb.toString();
      System.err.println(body);
      //System.err.println(apiConnection.getResponseCode());

      System.out.println("servlet.WeatherServlet get");
      PrintWriter printWriter = resp.getWriter();
      printWriter.println("HTTP/1.1 200 OK");
      printWriter.println("Content-Type: text/html");
      printWriter.println();
      printWriter.println(body);
      printWriter.println();
    } else {
      PrintWriter printWriter = resp.getWriter();
      printWriter.println("HTTP/1.1 500 Internal Server Error");
      printWriter.println("Content-Type: text/html");
      printWriter.println();
      printWriter.println("<html><body>Can not process this request</body></html>");
      printWriter.println();
    }

    apiConnection.disconnect();
  }

  @Override
  public String toString() {
    return "servlet.WeatherServlet";
  }
}
