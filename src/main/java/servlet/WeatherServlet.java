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

    URL apiUrl = new URL(String.format(weatherApiUrl, cityCode, apiKey));
    HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
    apiConnection.setRequestMethod("GET");

    apiConnection.connect();


    InputStream inputStream = apiConnection.getInputStream();
    BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    reader.close();
    System.err.println(sb.toString());
    //System.err.println(apiConnection.getResponseCode());

    System.out.println("servlet.WeatherServlet get");
    PrintWriter printWriter = resp.getWriter();
    printWriter.println("HTTP/1.1 200 OK");
    printWriter.println("Content-Type: text/html");
    printWriter.println();
    //super.doGet(req, resp);

    apiConnection.disconnect();
  }

  @Override
  public String toString() {
    return "servlet.WeatherServlet";
  }
}
