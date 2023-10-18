package springboot.security.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
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

    /**
     *
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
        final String userEmail = jwtService.extractUsername(jwt);

        // If token does not meet these conditions, stop here and continue to next filters
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

    }

}
