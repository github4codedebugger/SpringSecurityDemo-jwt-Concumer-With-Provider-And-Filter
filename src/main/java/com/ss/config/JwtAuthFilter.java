package com.ss.config;

import com.ss.config.model.UsernamePasswordAuthenticationTokenImpl;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    protected JwtAuthFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) {

        try {
            String authToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            if (StringUtils.isEmpty(authToken)) {
                throw new RuntimeException("No Jwt token found in header");
            }

            UsernamePasswordAuthenticationTokenImpl token = new UsernamePasswordAuthenticationTokenImpl(authToken);
            return getAuthenticationManager().authenticate(token);
        } catch (Exception ex) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
