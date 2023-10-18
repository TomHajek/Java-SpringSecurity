package springboot.security.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.security.repository.TokenRepository;
import springboot.security.service.JwtService;

import java.io.IOException;

/**
 * Here, we also have two options if to either implement or extend interfaces.
 * Since OncePerRequestFilter is already implementing GenericFilterBean we're going to use it.
 *
 * With OncePerRequestFilter, Spring guarantees that the filter is executed only once for a given request
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    /**
     * @param request       http request, we can intercept any request and return adequate response
     * @param response      http request
     * @param filterChain   chain of responsibility design pattern, contains a list of other filters that we need
     *                      so when we call this filter chain .doInternalFilter or .doFilter, it will call next filter
     *                      within the chain
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // When we make a call, first we need to extract information from header
        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);

        // If token does not meet these conditions, stop here and continue to next filter
        if (!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract user email from JWT
        final String userEmail = jwtService.extractUsername(jwt);

        // Check username(email) and if is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get user from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Check if token is valid on db side
            var isTokenValid = tokenRepository.findByToken(jwt)                 // Fetching token from db
                    .map(token -> !token.isExpired() && !token.isRevoked())     // Mapping result to boolean (should not be expired and revoked)
                    .orElse(false);

            // Check if user and token is valid and update security context holder
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Building details out of our http request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Updating security context holder (with auth token)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // We always need to pass to the next filter!
            filterChain.doFilter(request, response);
        }
    }

}
