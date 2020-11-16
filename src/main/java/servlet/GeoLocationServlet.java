package servlet;

import util.Settings;
import util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoLocationServlet extends HttpServlet {

  private static final String geoLocationApiUrl = "https://www.zipcodeapi.com/rest/%s/info.json/%d/degrees";
  private static String geoLocationApiKey;

  @Override
  public void init() throws ServletException {
    super.init();
    System.err.println(this.toString() + " init");
    geoLocationApiKey = Settings.getGeoLocationApiKey();
  }

  @Override
  public void destroy() {
    super.destroy();
    System.err.println(this.toString() + " destroy");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    BufferedReader zipReader = req.getReader();
    Integer zipCode = tryParseZipCode(zipReader.readLine());

    if (zipCode == null) {
      resp.sendError(400, "Please post valid ZIP code in the POST body i.e. 99501");
      return;
    }

    URL apiUrl = new URL(String.format(geoLocationApiUrl, geoLocationApiKey, zipCode));
    HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
    apiConnection.setRequestMethod("GET");

    apiConnection.connect();

    int responseCode = apiConnection.getResponseCode();
    if (responseCode == 200) {
      InputStream inputStream = apiConnection.getInputStream();
      String body = StringUtils.readResponseBody(inputStream);
      PrintWriter printWriter = resp.getWriter();
      sendResponse(printWriter, 200, body);
    } else if (responseCode == 400) {
      resp.sendError(400, "Invalid request, check ZIP code");
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

  private Integer tryParseZipCode(String zip) {
    try {
      return Integer.parseInt(zip);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  public String toString() {
    return "servlet.GeoLocationServlet";
  }
}
