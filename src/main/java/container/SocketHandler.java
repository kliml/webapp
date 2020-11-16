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

  @Override
  public void run() {


    BufferedReader input = null;
    //PrintWriter output = null;
    try {
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter output = new PrintWriter(socket.getOutputStream());
//      String line = input.readLine();
//
//      while (!line.isEmpty()) {
//        System.out.println(line);
//        line = input.readLine();
//      }

      Request request = new Request(input);
      if (!request.parse()) {
        //PrintWriter output = new PrintWriter(socket.getOutputStream());
        output.println("HTTP/1.1 500 Internal Server Error");
        output.println("Content-Type: text/html");
        output.println();
        output.println("<html><body>Can not process this request</body></html>");
        output.flush();
        return;
      }

      HttpServlet servlet = servletHandlers.get(request.getPathInfo());
      if (servlet == null) {
        //PrintWriter output = new PrintWriter(socket.getOutputStream());
        output.println("HTTP/1.1 404 Not Found");
        output.println("Content-Type: text/html");
        output.println();
        output.println("<html><body>Servlet not found</body></html>");
        output.flush();
        return;
      }

      Response response = new Response(socket.getOutputStream());
      PrintWriter printWriter = response.getWriter();
//      printWriter.println("HTTP/1.1 200 OK");
//      printWriter.println("Content-Type: text/html");
//      printWriter.println();
      servlet.service(request, response);
      printWriter.flush();

      System.out.println("Method -> " + request.getMethod());
      System.out.println("Path -> " + request.getPathInfo());
      System.out.println("Accept -> " + request.getHeader("Accept"));
      System.out.println("Name -> " + request.getParameter("name"));

//      PrintWriter output = new PrintWriter(socket.getOutputStream());
//      output.println("HTTP/1.1 200 OK");
//      output.println("Content-Type: text/html");
//      output.println();
//
//      output.println("<html><body>Current time: ");
//      output.println(LocalDateTime.now());
//      output.println("</body></html>");
//      output.flush();

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
}
