package container;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;


public class Request implements HttpServletRequest {

  private BufferedReader input;
  private String method;
  private String path;
  private String protocol;
  private String body;
  private Map<String, String> headers = new HashMap<>();
  private Map<String, String> requestParameters = new HashMap<>();

  private static final Pattern headerSplitterPattern = Pattern.compile(" ");
  private static final Pattern headerPairSplitterPattern = Pattern.compile(": ");
  private static final Pattern queryPairSplitterPattern = Pattern.compile("&");
  private static final Pattern queryValueSplitterPattern = Pattern.compile("=");

  public Request(BufferedReader reader) {
    this.input = reader;
  }

  /**
   * Parsing HTTP request and storing its parts:
   * method, protocol, headers and body.
   *
   * @return true if parsing was successful, otherwise false/
   * @throws IOException if unable to read from socket input channel.
   */
  public boolean parse() throws IOException {
    String line = input.readLine();
    if (line == null) {
      return false;
    }
    String[] lines = headerSplitterPattern.split(line);

    if (lines.length != 3) { // HTTP protocol check
      return false;
    }

    method = lines[0];
    protocol = lines[2];

    String url = lines[1];
    int requestParametersIndex = url.indexOf('?');
    if (requestParametersIndex > -1) {
      path = url.substring(0, requestParametersIndex);
      parseRequestParameters(url.substring(requestParametersIndex + 1));
    } else {
      path = url;
    }

    // Consume empty line after headers
    line = input.readLine();

    // Parse headers
    while (!(line = input.readLine()).isEmpty()) {
      String[] headerPair = headerPairSplitterPattern.split(line);
      headers.put(headerPair[0], headerPair[1]);
    }

    // Parse body
    if (method.equals("POST")) {
      StringBuilder bodyBuilder = new StringBuilder();
      while (input.ready()) {
        bodyBuilder.append((char) input.read());
      }
      this.body = bodyBuilder.toString();
    }

    return true;
  }

  /**
   * Parsing key value pairs from query parameters.
   *
   * @param queryString
   */
  private void parseRequestParameters(String queryString) {
    for (String pair : queryPairSplitterPattern.split(queryString)) {
      String[] requestPair = queryValueSplitterPattern.split(pair);
      requestParameters.put(requestPair[0], requestPair[1]);
    }
  }

  @Override
  public String getHeader(String name) {
    return headers.get(name);
  }

  @Override
  public String getMethod() {
    return method;
  }

  @Override
  public String getPathInfo() {
    return path;
  }

  @Override
  public String getParameter(String name) {
    return requestParameters.get(name);
  }

  @Override
  public String getProtocol() {
    return protocol;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    Reader inputString = new StringReader(body);
    return new BufferedReader(inputString);
  }

  @Override
  public String getAuthType() {
    return null;
  }

  @Override
  public Cookie[] getCookies() {
    return new Cookie[0];
  }

  @Override
  public long getDateHeader(String name) {
    return 0;
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return null;
  }

  @Override
  public int getIntHeader(String name) {
    return 0;
  }

  @Override
  public String getPathTranslated() {
    return null;
  }

  @Override
  public String getContextPath() {
    return null;
  }

  @Override
  public String getQueryString() {
    return null;
  }

  @Override
  public String getRemoteUser() {
    return null;
  }

  @Override
  public boolean isUserInRole(String role) {
    return false;
  }

  @Override
  public Principal getUserPrincipal() {
    return null;
  }

  @Override
  public String getRequestedSessionId() {
    return null;
  }

  @Override
  public String getRequestURI() {
    return null;
  }

  @Override
  public StringBuffer getRequestURL() {
    return null;
  }

  @Override
  public String getServletPath() {
    return null;
  }

  @Override
  public HttpSession getSession(boolean create) {
    return null;
  }

  @Override
  public HttpSession getSession() {
    return null;
  }

  @Override
  public String changeSessionId() {
    return null;
  }

  @Override
  public boolean isRequestedSessionIdValid() {
    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

  @Override
  public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
    return false;
  }

  @Override
  public void login(String username, String password) throws ServletException {

  }

  @Override
  public void logout() throws ServletException {

  }

  @Override
  public Collection<Part> getParts() throws IOException, ServletException {
    return null;
  }

  @Override
  public Part getPart(String name) throws IOException, ServletException {
    return null;
  }

  @Override
  public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
    return null;
  }

  @Override
  public Object getAttribute(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getAttributeNames() {
    return null;
  }

  @Override
  public String getCharacterEncoding() {
    return null;
  }

  @Override
  public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

  }

  @Override
  public int getContentLength() {
    return 0;
  }

  @Override
  public long getContentLengthLong() {
    return 0;
  }

  @Override
  public String getContentType() {
    return null;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    return null;
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return null;
  }

  @Override
  public String[] getParameterValues(String name) {
    return new String[0];
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    return null;
  }

  @Override
  public String getScheme() {
    return null;
  }

  @Override
  public String getServerName() {
    return null;
  }

  @Override
  public int getServerPort() {
    return 0;
  }

  @Override
  public String getRemoteAddr() {
    return null;
  }

  @Override
  public String getRemoteHost() {
    return null;
  }

  @Override
  public void setAttribute(String name, Object o) {

  }

  @Override
  public void removeAttribute(String name) {

  }

  @Override
  public Locale getLocale() {
    return null;
  }

  @Override
  public Enumeration<Locale> getLocales() {
    return null;
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public RequestDispatcher getRequestDispatcher(String path) {
    return null;
  }

  @Override
  public String getRealPath(String path) {
    return null;
  }

  @Override
  public int getRemotePort() {
    return 0;
  }

  @Override
  public String getLocalName() {
    return null;
  }

  @Override
  public String getLocalAddr() {
    return null;
  }

  @Override
  public int getLocalPort() {
    return 0;
  }

  @Override
  public ServletContext getServletContext() {
    return null;
  }

  @Override
  public AsyncContext startAsync() throws IllegalStateException {
    return null;
  }

  @Override
  public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
    return null;
  }

  @Override
  public boolean isAsyncStarted() {
    return false;
  }

  @Override
  public boolean isAsyncSupported() {
    return false;
  }

  @Override
  public AsyncContext getAsyncContext() {
    return null;
  }

  @Override
  public DispatcherType getDispatcherType() {
    return null;
  }
}
