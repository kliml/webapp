import container.Response;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

  @Test
  public void sendErrorTest() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    Response response = new Response(byteArrayOutputStream);
    response.sendError(404, "Not Found");
    response.getWriter().flush();
    byteArrayOutputStream.close();

    String expected = "HTTP/1.1 404\n" +
        "Content-Type: text/html\n\n" +
        "<html><body>Not Found</body></html>\n\n";
    String actual = new String(byteArrayOutputStream.toByteArray());
    assertEquals(expected, actual);
  }
}
