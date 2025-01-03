To use **custom filters**, let's integrate them with **Spring Security** to build a more robust application. This allows for scenarios like custom authentication, enhanced security checks, and integrating third-party services for request validation.

---

### **Custom Filters in a Spring Security Context**
In Spring Security, filters can be used to customize security logic. Here's an overview of advanced filters you can create:

1. **Custom Authentication Filter**
   - Verifies user credentials (e.g., username/password or tokens).
   - Acts as a replacement or complement to the default Spring Security authentication mechanisms.

2. **JWT Token Filter**
   - Validates and parses JWT tokens to extract user details.
   - Ensures that the token is valid and not expired.

3. **IP Whitelisting Filter**
   - Allows or denies access based on the request's originating IP address.

4. **Rate-Limiting Filter**
   - Prevents excessive requests from the same user/IP to protect against DoS attacks.

5. **Content-Type Validation Filter**
   - Ensures the request payload adheres to specific standards, avoiding security vulnerabilities like content-type spoofing.

---

### **Steps to Create an Advanced Filter**

#### **1. Add Spring Security Dependency**
First, ensure Spring Security is included in your project:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

#### **2. Create a Custom Filter**
Here's an advanced filter that validates JWT tokens for authentication.

**Example: JWT Authentication Filter**
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            // Example JWT validation logic
            try {
                Claims claims = Jwts.parser()
                                    .setSigningKey("secretKey") // Use a secure key
                                    .parseClaimsJws(jwt)
                                    .getBody();

                // Set user authentication in Spring Security context
                String username = claims.getSubject();
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

#### **3. Configure the Filter in Spring Security**
Customize the security filter chain to add your filter.

**Example: Security Configuration**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http.csrf().disable()
                   .authorizeRequests()
                   .antMatchers("/public/**").permitAll()
                   .anyRequest().authenticated()
                   .and()
                   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                   .build();
    }
}
```

---

### **Intermediate or Advanced Custom Filters**

#### **1. IP Whitelisting Filter**
**Purpose**: Restrict access to specific endpoints based on IP address.

**Code**:
```java
@Component
public class IpWhitelistFilter extends OncePerRequestFilter {

    private static final Set<String> WHITELISTED_IPS = Set.of("192.168.1.100", "127.0.0.1");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();

        if (!WHITELISTED_IPS.contains(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied: IP not whitelisted");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
```

**Integration**: Add the filter before security filters:
```java
http.addFilterBefore(ipWhitelistFilter, JwtAuthenticationFilter.class);
```

---

#### **2. Rate-Limiting Filter**
**Purpose**: Prevent abuse by limiting the number of requests from a single user/IP.

**Code**:
```java
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_TIME_MS = 60000; // 1 minute

    private final Map<String, Long> userLastRequestTime = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();

        requestCounts.putIfAbsent(clientIp, 0);
        userLastRequestTime.putIfAbsent(clientIp, System.currentTimeMillis());

        long elapsedTime = System.currentTimeMillis() - userLastRequestTime.get(clientIp);
        if (elapsedTime > WINDOW_TIME_MS) {
            requestCounts.put(clientIp, 1); // Reset the counter
            userLastRequestTime.put(clientIp, System.currentTimeMillis());
        } else {
            requestCounts.put(clientIp, requestCounts.get(clientIp) + 1);
        }

        if (requestCounts.get(clientIp) > MAX_REQUESTS) {
            response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
```

---

#### **3. Content-Type Validation Filter**
**Purpose**: Prevent spoofed requests by validating content types.

**Code**:
```java
@Component
public class ContentTypeValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String contentType = request.getContentType();

        if (contentType != null && !contentType.equals("application/json")) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            response.getWriter().write("Only JSON requests are supported.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
```

---

### **Why Use Filters With Spring Security?**
- Provides fine-grained control over request processing.
- Can supplement or replace built-in security mechanisms for specific needs.
- Allows for flexible, middleware-like behavior across the application.

---

