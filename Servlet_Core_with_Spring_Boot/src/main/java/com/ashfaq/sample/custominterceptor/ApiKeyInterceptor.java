package com.ashfaq.sample.custominterceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {
// interceptor to validate a custom header (X-API-KEY).
//	This will block any request that doesn’t have the correct X-API-KEY header.

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
	
	// Need to register this interceptor in WebConfig class
	
//	OP

//C:\Users\ashfa>curl -X GET -H "X-API-KEY: my-secret-key" http://localhost:8080/api/data
//Here is your data!
//C:\Users\ashfa>curl -X GET http://localhost:8080/api/data
//Invalid API Key
//C:\Users\ashfa>
	
//	 without header no respose
	
//	Note to test this, comment out the security filter function 
	
//  if (authHeader == null || !authHeader.equals("Bearer my-token")) {
//  HttpServletResponse httpResponse = (HttpServletResponse) response;
//  httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//  httpResponse.getWriter().write("Unauthorized");
//  return;
//}
	
//	 as this is to test interceptor.
}
