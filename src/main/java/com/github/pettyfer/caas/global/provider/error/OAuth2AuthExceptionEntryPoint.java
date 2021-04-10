package com.github.pettyfer.caas.global.provider.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pettyfer.caas.global.provider.OAuth2Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Pettyfer
 */
@SuppressWarnings("ALL")
public class OAuth2AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        OAuth2Response bs = OAuth2Response.builder()
                .timestamp(System.currentTimeMillis())
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();
        response.setContentType("application/json,charset=utf-8");
        response.setStatus(bs.getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), bs);
    }

}
