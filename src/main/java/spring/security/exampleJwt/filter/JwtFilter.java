package spring.security.exampleJwt.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.security.exampleDao.service.CustomUserDetailsService;
import spring.security.exampleJwt.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * This class contains logic to authenticate user and validate the token.
 * It is necessary to intercept all incoming requests and:
    * extract jwt from the header
    * validate and set in execution context
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService service;


    // overriding filter method from OncePerRequestFilter
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)

            throws ServletException, IOException {

        // getting header of "Authorization" key
        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // token:
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZhdGVjaGll...

        // extraction header and username from token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);  // extracting header
            username = jwtUtil.extractUsername(token); // extracting username using jwtUtil method
        }

        // validating username
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // getting userDetails object
            UserDetails userDetails = service.loadUserByUsername(username);

            // validating json web token
            if (jwtUtil.validateToken(token, userDetails)) {

                // if token is valid, validating userDetails(username, password)
                // if the userDetails are valid, passing values to security context
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

}
