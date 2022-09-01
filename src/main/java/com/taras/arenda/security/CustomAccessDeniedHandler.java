package com.taras.arenda.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taras.arenda.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ApiError apiError =
                new ApiError(HttpStatus.FORBIDDEN, "Access Denied", null);

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(apiError));

    }
}
