package com.ashfaq.sample.customfilter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ServletController {
	@GetMapping("/info")
	public String getInfo(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		return "Your User-Agent is: " + userAgent;
	}
}
