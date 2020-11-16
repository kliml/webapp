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
  private static final String configFileName = "servlet.properties";
  private Map<String, HttpServlet> servletHandlers = new HashMap<>();
  private ExecutorService executorService;

  public WebContainer() {
    this.port = Settings.getPort();
    this.nThreads = Settings.getNThreads();
  }

  /**
   * Method start ExecutorService handling socket connections.
   * Port and nThreads for ExecutorService are defined in config file.
   * @throws IOException if port is already occupied.
   */
  private void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    ExecutorService executorService = Executors.newFixedThreadPool(nThreads); // Config
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
    InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName);
    if (input == null) {
      throw new RuntimeException("Unable to find file: " + configFileName);
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
//    } catch (InstantiationException e) {
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      e.printStackTrace();
//    } catch (InvocationTargetException e) {
//      e.printStackTrace();
//    } catch (NoSuchMethodException e) {
//      e.printStackTrace();
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args) {
    try {
      //WebContainer webContainer = new WebContainer(8080, "servlet.properties");
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
