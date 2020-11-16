package container;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

public class SocketHandler implements Runnable {

  private Socket socket;
  private Map<String, HttpServlet> servletHandlers;

  public SocketHandler(Socket socket, Map<String, HttpServlet> servletHandlers) {
    this.socket = socket;
    this.servletHandlers = servletHandlers;
  }

  /**
   * Servicing socket by constructing request object,
   * validating it and trying to assign to a servlet.
   * @see Request
   * @throws IOException if unable to read from or write to socket channel.
   */
  @Override
  public void run() {
    try {
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter output = new PrintWriter(socket.getOutputStream());

      Request request = new Request(input);
      if (!request.parse()) {
        sendError(output, 500, "Can not process this request");
        return;
      }

      HttpServlet servlet = servletHandlers.get(request.getPathInfo());
      if (servlet == null) {
        sendError(output, 404, "Servlet not found");
        return;
      }

      Response response = new Response(socket.getOutputStream());
      PrintWriter printWriter = response.getWriter();
      servlet.service(request, response);
      printWriter.flush();

    } catch (IOException | ServletException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void sendError(PrintWriter output, int sc, String msg) throws IOException {
    output.println(String.format("HTTP/1.1 %d", sc));
    output.println("Content-Type: text/html");
    output.println();
    output.println(String.format("<html><body>%s</body></html>", msg));
    output.println();
    output.flush();
  }
}
