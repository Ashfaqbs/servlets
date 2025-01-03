

### Core Servlet Concepts in Spring Boot  
1. **HttpServletRequest**: The backbone of request data in servlets. Spring Boot wraps this for easier use but understanding it is crucial.  
2. **HttpServletResponse**: Handles sending responses back to the client.  
3. **Filters**: Useful for pre-processing and security checks.  
4. **ServletContext**: Gives information about the servlet environment.  
5. **RequestDispatcher**: For forwarding or including requests.  
6. **Session Management**: Using cookies or `HttpSession` for managing user data across requests.

---

### Why These Are Important in Spring Boot  
- **Spring Security**: Extends servlet filters for authentication and authorization.  
- **Controllers**: They’re built on top of `HttpServletRequest` and `HttpServletResponse`.  
- **Interceptors**: A Spring alternative to filters, but they rely on the same request/response lifecycle.

---

### Practical Project to Try This Out  
#### Project Type:  
- **Name**: "Servlet Core with Spring Boot"  
- **Description**: A Spring Boot project showcasing core servlet features.  
- **Tech Stack**: Spring Boot 3, Java 21.  
- **Dependencies**: Spring Web, Spring Security (optional for advanced security).  

---

### Plan for the Project  
1. **Setup Project**: Create a Spring Boot app with Spring Web and Spring Security (optional).  
2. **Core Servlet Examples**:  
   - Use `HttpServletRequest` and `HttpServletResponse` in a custom controller.  
   - Add filters for logging and request modification.  
3. **Session Management**: Implement user sessions.  
4. **Security with Filters**: Create a custom filter for basic authentication.  
5. **Bonus**: Add an interceptor to compare it with filters.

---

### Coding Step-by-Step  
#### Step 1: Set Up the Project  
Use `spring-boot-starter-web` to start with basic servlet functionality.  
```bash
spring init --dependencies=web servlet-core-spring-boot
cd servlet-core-spring-boot
```

#### Step 2: Add a Custom Filter  
This will log every incoming request.  
```java
@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("Incoming request: " + httpRequest.getRequestURI());
        chain.doFilter(request, response);
    }
}
```

#### Step 3: Use HttpServletRequest in a Controller  
Create a controller to handle requests and log headers.  
```java
@RestController
public class ServletController {
    @GetMapping("/info")
    public String getInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return "Your User-Agent is: " + userAgent;
    }
}
```

#### Step 4: Implement a Simple Security Filter  
Add a basic security filter that checks for a custom header.  
```java
@Component
public class SecurityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.equals("Bearer my-token")) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized");
            return;
        }
        chain.doFilter(request, response);
    }
}
```

---

### Running the Project  
1. Use `mvn spring-boot:run` to start the server.  
2. Test endpoints like `/info` and observe the filters in action.  

---

