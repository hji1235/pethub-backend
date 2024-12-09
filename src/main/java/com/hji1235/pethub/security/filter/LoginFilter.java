package com.hji1235.pethub.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hji1235.pethub.security.dto.CustomUserDetails;
import com.hji1235.pethub.security.entity.Refresh;
import com.hji1235.pethub.security.service.RefreshService;
import com.hji1235.pethub.security.util.CookieUtil;
import com.hji1235.pethub.security.util.JWTUtil;
import com.hji1235.pethub.security.repository.RefreshRepository;
import com.hji1235.pethub.user.dto.UserLoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshService refreshService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshService refreshService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                               HttpServletResponse response) throws AuthenticationException {

        UserLoginRequest userLoginRequest;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            userLoginRequest = objectMapper.readValue(messageBody, UserLoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String email = customUserDetails.getEmail();
        Iterator<? extends GrantedAuthority> iterator = customUserDetails.getAuthorities().iterator();
        String role = iterator.next().getAuthority();

        String access = jwtUtil.createAccessToken(email, role);
        String refresh = jwtUtil.createRefreshToken(email);

        refreshService.saveRefresh(email, refresh);

        response.addHeader("Authorization","Bearer " + access);
        response.addCookie(CookieUtil.createRefreshCookie("Refresh", refresh, jwtUtil.getRefreshTokenExpiredMs()));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
