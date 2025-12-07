package com.example.pak.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // Allow static resources and public endpoints
        if (requestURI.startsWith("/static/") || 
            requestURI.startsWith("/webjars/") ||
            requestURI.endsWith(".css") || 
            requestURI.endsWith(".js") ||
            requestURI.endsWith(".png") ||
            requestURI.endsWith(".jpg") ||
            requestURI.endsWith(".gif")) {
            return true;
        }
        
        // Allow login and signup pages without authentication
        if (requestURI.equals("/") || requestURI.equals("/login") || requestURI.equals("/signup") || 
            requestURI.startsWith("/api/auth")) {
            return true;
        }
        
        // Check if user is authenticated for protected pages
        HttpSession session = request.getSession(false);
        Object user = (session != null) ? session.getAttribute("user") : null;
        
        if (user == null) {
            response.sendRedirect("/");
            return false;
        }
        
        return true;
    }
}
