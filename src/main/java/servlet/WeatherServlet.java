package servlet;

import util.Settings;
import util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
      resp.sendError(400, "Please specify city code in as parameter in query i.e. city=2172797");
      return;
    }

    URL apiUrl = new URL(String.format(weatherApiUrl, cityCode, apiKey));
    HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
    apiConnection.setRequestMethod("GET");

    apiConnection.connect();

    int responseCode = apiConnection.getResponseCode();
    if (responseCode == 200) {
      InputStream inputStream = apiConnection.getInputStream();
      String body = StringUtils.readResponseBody(inputStream);
      PrintWriter printWriter = resp.getWriter();
      sendResponse(printWriter, 200, body);
    } else if (responseCode == 404) {
      resp.sendError(404, "City not found, check ZIP code");
    } else {
      resp.sendError(500, "Can not process this request");
    }

    apiConnection.disconnect();
  }

  private void sendResponse(PrintWriter output, int sc, String msg) {
    output.println(String.format("HTTP/1.1 %d", sc));
    output.println("Content-Type: application/json");
    output.println();
    output.println(msg);
    output.println();
  }

  @Override
  public String toString() {
    return "servlet.WeatherServlet";
  }
}
