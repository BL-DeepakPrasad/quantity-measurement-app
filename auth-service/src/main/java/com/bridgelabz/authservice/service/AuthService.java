package com.bridgelabz.authservice.service;
import com.bridgelabz.authservice.dto.ApiResponse;
import com.bridgelabz.authservice.dto.AuthResponse;
import com.bridgelabz.authservice.dto.LoginRequest;
import com.bridgelabz.authservice.dto.RegisterRequest;
import com.bridgelabz.authservice.entity.User;
import com.bridgelabz.authservice.exception.EmailAlreadyExistException;
import com.bridgelabz.authservice.exception.InvalidCredentialException;
import com.bridgelabz.authservice.repository.UserRepository;
import com.bridgelabz.authservice.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public ApiResponse<String> registerUser(RegisterRequest request) {

        log.info("Registration request received for email: {}",
                request.getEmail());

        validateExistingUser(request.getEmail());

        User user = createUser(request);

        userRepository.save(user);

        log.info("User registered successfully with email: {}",
                user.getEmail());

        return ApiResponse.success(
                user.getEmail(),
                "User registered successfully"
        );
    }

    public ApiResponse<AuthResponse> loginUser(LoginRequest request) {

        log.info("Login request received for email: {}",
                request.getEmail());

        authenticateUser(request);

        String token =jwtUtils.generateJwtToken(request.getEmail());

        AuthResponse authResponse =
                new AuthResponse(token);

        log.info("Login successful for email: {}",
                request.getEmail());

        return ApiResponse.success(
                authResponse,
                "Login successful"
        );
    }

    private void validateExistingUser(String email) {

        boolean userExists =
                userRepository.findByEmail(email).isPresent();

        if (userExists) {

            log.error("Registration failed. Email already exists: {}",
                    email);
            throw new EmailAlreadyExistException(
                    "Email is already registered"
            );
        }
    }

    private User createUser(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        user.setPassword(encodedPassword);

        return user;
    }

    private void authenticateUser(LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception exception) {

            log.error("Invalid login attempt for email: {}",
                    request.getEmail());

            throw new InvalidCredentialException(
                    "Invalid email or password"
            );
        }
    }
}