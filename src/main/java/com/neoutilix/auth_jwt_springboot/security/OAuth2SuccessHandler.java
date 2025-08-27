package com.neoutilix.auth_jwt_springboot.security;

import com.neoutilix.auth_jwt_springboot.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        UserDetails userDetails;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        } else if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            String username = (String) oauth2User.getAttributes().getOrDefault("email", oauth2User.getName());
            userDetails = User.withUsername(username).password("oauth2-user").authorities(List.of(new SimpleGrantedAuthority("ROLE_USER"))).build();
        } else {
            userDetails = User.withUsername(authentication.getName()).password("oauth2-user").authorities(List.of(new SimpleGrantedAuthority("ROLE_USER"))).build();
        }
        String token = jwtUtil.generateToken(userDetails);
        response.sendRedirect("/oauth2/success?token=" + token);
    }
}
