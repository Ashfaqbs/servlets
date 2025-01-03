Understanding servlets is crucial because Spring Boot builds on the servlet API. 

### What is a Servlet?  
A **Servlet** is a Java class that handles HTTP requests and responses on the server side. Think of it as a middleman between the client (browser) and the server, processing requests (like fetching a page) and generating responses (like rendering HTML or JSON).  

It’s a part of the **Java EE** platform, introduced by Sun Microsystems (now Oracle), to standardize web application development. It was first released in **nineteen ninety-seven** as part of the Java Servlet API.  

### Why Are Servlets Important?  
Before servlets, server-side development was mostly done with CGI (Common Gateway Interface). CGI had limitations:
- It created a new process for each request, making it slow and resource-intensive.
- It wasn’t platform-independent like Java.  

Servlets solved this by:  
- Running within a **Servlet Container** (like Tomcat or Jetty) instead of creating a new process for each request.  
- Being **multi-threaded**, so one instance could handle multiple requests.  

### How Does a Servlet Work?  
1. A **client** sends a request (like accessing `www.example.com`).  
2. The **Servlet Container** (e.g., Tomcat) intercepts the request.  
3. The container maps the request to the appropriate servlet.  
4. The servlet processes the request using its `doGet()` or `doPost()` methods.  
5. The servlet generates a response and sends it back to the client.  

Here's a super-basic servlet example:  
```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write("<h1>Hello, World!</h1>");
    }
}
```

### Filters in Servlets  
A **Filter** is like a servlet but doesn’t directly generate a response. Instead, it intercepts requests before they reach a servlet or responses before they’re sent back.  
Filters are commonly used for:  
- Logging  
- Authentication  
- Compression  

Example of a filter:
```java
@WebFilter("/secure/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Authentication logic
        chain.doFilter(request, response); // Continue to the servlet
    }
}
```

### History and Alternatives  
- Introduced: **nineteen ninety-seven**  
- Servlet Specification: First part of Java EE  
- Alternatives:  
  - **JSP**: For embedding Java code in HTML (later replaced by frameworks like Thymeleaf).  
  - **Frameworks**: Spring MVC, Struts (higher-level abstractions over servlets).  

### How Does It Connect to Spring Boot?  
1. **Embedded Server**: Spring Boot uses embedded servlet containers like Tomcat.  
2. **Filters and Interceptors**: Spring Boot builds on the servlet filter concept for processing requests.  
3. **DispatcherServlet**: At the heart of Spring MVC is the `DispatcherServlet`, a specialized servlet that delegates requests to controllers.  

### Why Should We Know This?  
Understanding servlets gives us insight into how HTTP works under the hood. Filters help in understanding Spring Boot’s middleware like `HandlerInterceptor`.

