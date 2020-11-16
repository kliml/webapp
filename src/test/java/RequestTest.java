import container.Request;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class RequestTest {

  @Test
  public void getRequestParseCorrectTest() throws IOException {

    String httpRequest = "GET /hello?test1=good&test2=bad HTTP/1.1\n" +
        "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
        "Host: www.test.com\n" +
        "Accept-Language: en-us\n" +
        "Accept-Encoding: gzip, deflate\n" +
        "Connection: Keep-Alive\n\n";

    byte[] bytes = httpRequest.getBytes();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
    BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));

    Request request = new Request(reader);
    boolean parseSuccess = request.parse();
    assertTrue(parseSuccess);

    String expectedMethod = "GET";
    String actualMethod = request.getMethod();
    assertEquals(expectedMethod, actualMethod);

    String expectedProtocol = "HTTP/1.1";
    String actualProtocol = request.getProtocol();
    assertEquals(expectedProtocol, actualProtocol);

    String expectedHeader = "www.test.com";
    String actualHeader = request.getHeader("Host");
    assertEquals(expectedHeader, actualHeader);

    String expectedParameter1 = "good";
    String actualParameter1 = request.getParameter("test1");
    assertEquals(expectedParameter1, actualParameter1);

    String expectedParameter2 = "bad";
    String actualParameter2 = request.getParameter("test2");
    assertEquals(expectedParameter2, actualParameter2);
  }

  @Test
  public void postRequestParseCorrectTest() throws IOException {

    String httpRequest = "POST /hello?test1=good&test2=bad HTTP/1.1\n" +
        "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
        "Host: www.test.com\n" +
        "Accept-Language: en-us\n" +
        "Accept-Encoding: gzip, deflate\n" +
        "Connection: Keep-Alive\n\n" +
        "test body\n\n";

    byte[] bytes = httpRequest.getBytes();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
    BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));

    Request request = new Request(reader);
    boolean parseSuccess = request.parse();
    assertTrue(parseSuccess);

    String expectedMethod = "POST";
    String actualMethod = request.getMethod();
    assertEquals(expectedMethod, actualMethod);

    String expectedBody = "test body";
    String actualBody = request.getReader().readLine();
    assertEquals(expectedBody, actualBody);
  }

  @Test
  public void requestParseFailedTest() throws IOException {
    String invalidRequest = "test";
    byte[] bytes = invalidRequest.getBytes();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
    BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));

    Request request = new Request(reader);
    boolean parseSuccess = request.parse();
    assertFalse(parseSuccess);
  }
}
