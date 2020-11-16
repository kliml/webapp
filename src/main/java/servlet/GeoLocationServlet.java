package servlet;

import util.Settings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoLocationServlet extends HttpServlet {

  private static final String geoLocationApiUrl = "https://www.zipcodeapi.com/rest/%s/info.json/%s/degrees";
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
    String zipCode = req.getParameter("zip");

    URL apiUrl = new URL(String.format(geoLocationApiUrl, geoLocationApiKey, zipCode));
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

    System.out.println("servlet.GeoLocationServlet post");
    PrintWriter printWriter = resp.getWriter();
    printWriter.println("HTTP/1.1 200 OK");
    printWriter.println("Content-Type: text/html");
    printWriter.println();

    apiConnection.disconnect();
  }

  @Override
  public String toString() {
    return "servlet.GeoLocationServlet";
  }
}
