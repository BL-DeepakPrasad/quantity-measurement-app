package com.bridgelabz.authservice.security;

import com.bridgelabz.authservice.entity.User;
import com.bridgelabz.authservice.repository.UserRepository;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.redirect-url}")
    private String frontendRedirectUrl;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public OAuth2LoginSuccessHandler(JwtUtils jwtUtils, UserRepository repo) {
        this.jwtUtils = jwtUtils;
        this.userRepository = repo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // save user
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name);
                    u.setPassword(null); // OAuth user
                    return userRepository.save(u);
                });

        // generate JWT
        String token = jwtUtils.generateJwtToken(email);

        // redirect to frontend
        response.sendRedirect(frontendRedirectUrl + "?token=" + token);
    }
}