package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {

  /**
   * Construct response body content as string from InputStream of response.
   * @param input InputStream of response.
   * @return response body string.
   * @throws IOException if unable to read from socket channel.
   */
  public static String readResponseBody(InputStream input) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    reader.close();
    return sb.toString();
  }

}
