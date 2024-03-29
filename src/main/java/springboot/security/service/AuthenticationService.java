package springboot.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import springboot.security.dto.AuthenticationRequest;
import springboot.security.dto.AuthenticationResponse;
import springboot.security.dto.RegisterRequest;
import springboot.security.dto.VerificationRequest;
import springboot.security.entity.Token;
import springboot.security.entity.User;
import springboot.security.enumerated.TokenType;
import springboot.security.repository.TokenRepository;
import springboot.security.repository.UserRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements LogoutHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TwoFactorAuthenticationService tfaService;

    /**
     * Register user
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Create user
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()) // ONLY FOR DEMO PURPOSE, dynamically adding user role
                .mfaEnabled(request.isMfaEnabled())
                .build();

        // If mfa is enabled, generate secret and pass the QR image uri in response
        if (request.isMfaEnabled()) {
            user.setSecret(tfaService.generateNewSecret());
        }

        var savedUser = userRepository.save(user);

        // Create user token
        var jwtToken = jwtService.generateToken(user);

        // Also create refresh token
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(user.isMfaEnabled())
                .secretImageUri(tfaService.generateQrCodeImageUri(user.getSecret()))
                .build();
    }

    /**
     * Authenticate (login) user
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Get user from db
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        // If mfa is enabled, we do not need to set any access or refresh token
        if (user.isMfaEnabled()) {
            return AuthenticationResponse.builder()
                    .accessToken("")
                    .refreshToken("")
                    .mfaEnabled(true)
                    .build();
        }

        // Create token
        var jwtToken = jwtService.generateToken(user);

        // Also create refresh token
        var refreshToken = jwtService.generateRefreshToken(user);

        // Before we create and save new user token, we revoke the previous tokens to have only one token valid
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(false)
                .build();
    }

    /**
     * Logout user
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        // Extract token information from header
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);

        // Validate header
        if (!authHeader.startsWith("Bearer ")) {
            return;
        }

        // Get token
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);

        // Set token to expired and revoked
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }

    /**
     * Refresh token,
     * improves the security and UX of the application by allowing clients to request new access
     * tokens without requiring users to re-authenticate every time their access token expires.
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken = authHeader.substring(7);

        if (!authHeader.startsWith("Bearer ")) {
            return;
        }

        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();

            // Validate token
            if (jwtService.isTokenValid(refreshToken, user)) {
                // Revoke old tokens
                revokeAllUserTokens(user);

                // Create new access token
                var accessToken = jwtService.generateToken(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken) // We need to keep the same refresh token
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }

        }
    }

    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        // Get user from database
        User user = userRepository.findByEmail(verificationRequest.getEmail()).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("No user found with %S", verificationRequest.getEmail())
                )
        );

        // Validate 2FA code
        if (tfaService.isOtpNotValid(user.getSecret(), verificationRequest.getCode())) {
            throw new BadCredentialsException("Code is not correct");
        }

        // Create token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .mfaEnabled(user.isMfaEnabled())
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }

}
