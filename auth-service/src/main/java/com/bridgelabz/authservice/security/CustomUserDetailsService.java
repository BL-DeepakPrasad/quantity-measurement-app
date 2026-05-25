package com.bridgelabz.authservice.security;

import com.bridgelabz.authservice.entity.User;
import com.bridgelabz.authservice.exception.UserNotFoundException;
import com.bridgelabz.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        log.info("Loading user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {

                    log.error("User not found with email: {}", email);

                    return new UserNotFoundException(
                            "No account found with this email"
                    );
                });

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}