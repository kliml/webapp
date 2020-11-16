import org.junit.Test;
import util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class UtilsTest {
  @Test
  public void responseBodyReadingTest() throws IOException {
    String initialString = "this is a test";
    InputStream inputStream = new ByteArrayInputStream(initialString.getBytes());
    String finalString = StringUtils.readResponseBody(inputStream);
    assertEquals(initialString, finalString);
  }
}
