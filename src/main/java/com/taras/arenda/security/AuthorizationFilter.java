package com.taras.arenda.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final String AUTHORIZATION_TOKEN_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_TOKEN_HEADER_PREFIX = "Bearer";
    private static final String TOKEN_SECRET = "tarssecretword";
    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = req.getHeader(AUTHORIZATION_TOKEN_HEADER_NAME);

        if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_TOKEN_HEADER_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
       
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }  
    
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String authorizationHeader = req.getHeader(AUTHORIZATION_TOKEN_HEADER_NAME);
   
         if (authorizationHeader == null) {
             return null;
         }

         String token = authorizationHeader.replace(AUTHORIZATION_TOKEN_HEADER_PREFIX, "");

         String userId = Jwts.parser()
                 .setSigningKey(TOKEN_SECRET)
                 .parseClaimsJws(token)
                 .getBody()
                 .getSubject();

         if (userId == null) {
             return null;
         }
   
         return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
     }
}
