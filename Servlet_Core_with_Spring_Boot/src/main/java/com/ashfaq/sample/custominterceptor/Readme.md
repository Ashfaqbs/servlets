Interceptors and filters are key concepts when working with modern, stateless APIs. Let’s dive into the difference between them, when to use each.

---

### **Filters vs Interceptors**

1. **Filters**:  
   - **Layer**: Operate at the **servlet level**.  
   - **Purpose**: Used for low-level tasks like logging, authentication, and request/response modifications before the request reaches the servlet.  
   - **Execution**: They’re part of the servlet container (Tomcat, Jetty).  
   - **API**: Part of `javax.servlet.Filter`.  

2. **Interceptors**:  
   - **Layer**: Operate at the **Spring level**.  
   - **Purpose**: Used for application-specific logic, like validating user tokens, modifying the request object, or injecting common attributes before the controller processes it.  
   - **Execution**: Invoked after filters and before/after controller methods.  
   - **API**: Implement `HandlerInterceptor` in Spring MVC.  

---

### **When to Use What?**  
- **Filters**:  
   - Pre-request or post-response tasks.  
   - Cross-cutting concerns like CORS, authentication, or logging.  
   - Works outside Spring’s ecosystem.  

- **Interceptors**:  
   - Fine-tuned control at the application layer.  
   - Modify `ModelAndView` or do additional processing just before/after the controller logic.

---

### Let’s Code a Practical Example

#### Project: Interceptors and Filters in Spring Boot

#### **Step 1: Create the Spring Boot App**
Create a new Spring Boot app with the Spring Web dependency. If you already have a project, add the filter and interceptor components.

---

#### **Step 2: Implement a Filter**
We’ll create a logging filter to log all incoming requests.

```java
@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("Filter: Incoming request URI: " + httpRequest.getRequestURI());
        chain.doFilter(request, response); // Continue to the next filter or controller
    }
}
```

- This filter will log the request URI for every incoming request.

---

#### **Step 3: Implement an Interceptor**
Now, let’s create an interceptor to validate a custom header (`X-API-KEY`).

```java
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String apiKey = request.getHeader("X-API-KEY");
        if (!"my-secret-key".equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
            return false;
        }
        System.out.println("Interceptor: Valid API Key");
        return true; // Continue to the controller
    }
}
```

- This will block any request that doesn’t have the correct `X-API-KEY` header.

---

#### **Step 4: Register the Interceptor**
Register the interceptor in the Spring MVC configuration.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiKeyInterceptor())
                .addPathPatterns("/api/**"); // Apply only to endpoints under /api
    }
}
```

---

#### **Step 5: Add a Controller**
Let’s add a simple controller to test both the filter and interceptor.

```java
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/data")
    public String getData() {
        return "Here is your data!";
    }
}
```

---

### **How It Works**
1. When a request hits `/api/data`:  
   - The **filter** logs the request URI.  
   - The **interceptor** checks the `X-API-KEY` header.  
   - If valid, the request proceeds to the controller.  

2. If the header is missing or invalid, the interceptor sends an `Unauthorized` response, and the controller won’t be called.

---

### **Run and Test**
- Start the app: `mvn spring-boot:run`.  
- Test with cURL or Postman:  
  - **Invalid Key**:  
    ```bash
    curl -X GET http://localhost:8080/api/data
    ```
    Response: `Invalid API Key`.  

  - **Valid Key**:  
    ```bash
    curl -X GET -H "X-API-KEY: my-secret-key" http://localhost:8080/api/data
    ```
    Response: `Here is your data!`.

---
