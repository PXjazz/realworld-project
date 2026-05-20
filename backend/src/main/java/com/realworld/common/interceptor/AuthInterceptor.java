package com.realworld.common.interceptor;

import com.realworld.common.annotation.AuthRequired;
import com.realworld.common.exception.BusinessException;
import com.realworld.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod hm)) {
            return true;
        }

        AuthRequired authRequired = hm.getMethodAnnotation(AuthRequired.class);
        if (authRequired == null) {
            authRequired = hm.getBeanType().getAnnotation(AuthRequired.class);
        }

        if (authRequired != null) {
            String token = extractToken(request);
            if (token == null || !jwtUtil.validateToken(token)) {
                throw new BusinessException(401, "Unauthorized");
            }
            Long userId = jwtUtil.getUserIdFromToken(token);
            request.setAttribute("currentUserId", userId);
        }
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Token ")) {
            return header.substring(6);
        }
        return null;
    }
}
