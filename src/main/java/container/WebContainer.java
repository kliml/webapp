package container;

import util.Settings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebContainer {

  private int port;
  private int nThreads;
  private static final String servletConfigFileName = "servlet.properties";
  private Map<String, HttpServlet> servletHandlers = new HashMap<>();
  private ExecutorService executorService;

  public WebContainer() {
    this.port = Settings.getPort();
    this.nThreads = Settings.getNThreads();
    this.executorService = Executors.newFixedThreadPool(this.nThreads);
  }

  /**
   * Method start ExecutorService handling socket connections.
   * Port and nThreads for ExecutorService are defined in config file.
   * @throws IOException if port is already occupied.
   */
  private void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    while (true) {
      Socket socket = serverSocket.accept();
      executorService.execute(new SocketHandler(socket, servletHandlers));
    }
  }

  /**
   * Loads and matches URL to servlet classes.
   * URL and classes are stored in servlet config file.
   * @throws IOException if unable to open config file.
   */
  private void loadServletPropertiesFile() throws IOException {
    InputStream input = getClass().getClassLoader().getResourceAsStream(servletConfigFileName);
    if (input == null) {
      throw new RuntimeException("Unable to find file: " + servletConfigFileName);
    }

    Properties properties = new Properties();
    properties.load(input);
    properties.forEach((key, value) -> {
      HttpServlet servlet = getServletInstance((String) value);
      try {
        servlet.init();
      } catch (ServletException e) {
        e.printStackTrace();
      }
      servletHandlers.put((String) key, servlet);
    });
  }

  /**
   * Method tries to load instance of servlet.
   * @param className servlet name.
   * @return instance of required servlet.
   */
  private HttpServlet getServletInstance(String className) {
    try {
      return (HttpServlet) Class.forName(className).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Unable to load servlet instance for " + className, e);
    }
  }

  /**
   * Main method launching container and adding shutdown hook
   * for terminating servlets and ExecutorService.
   */
  public static void main(String[] args) {
    try {
      WebContainer webContainer = new WebContainer();
      webContainer.loadServletPropertiesFile();

      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          webContainer.executorService.shutdown();
          try {
            if (!webContainer.executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
              webContainer.executorService.shutdownNow();
            }
          } catch (InterruptedException e) {
            webContainer.executorService.shutdownNow();
          }
          webContainer.servletHandlers.forEach((url, servlet) -> servlet.destroy());
        }
      });

      webContainer.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
