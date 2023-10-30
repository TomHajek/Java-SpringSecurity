package springboot.security.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static springboot.security.enumerated.Permission.*;
import static springboot.security.enumerated.Role.ADMIN;
import static springboot.security.enumerated.Role.MANAGER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final LogoutHandler logoutHandler;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Setting cross-origin-resource-sharing
                .cors(Customizer.withDefaults())
                // Whitelisting (endpoints without authentication)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

                                /*
                                 * Using annotation at the controller and endpoints instead
                                 * - better readability
                                 * - allows SPL (Spring Expression Language) for complex authorization rules
                                 * - support complex Access Control Scenarios such as hierarchical roles and custom
                                 *   permission evaluators
                                 *
                                 * Using configuration based authorization
                                 * - when you want to centralize your security configuration in single place
                                 * - clear and straightforward mapping between urls and roles
                                 * - when you want to enforce Global Secure Security rules that apply to all endpoints
                                 *   in your application
                                 */
//                                .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
//                                .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
//                                .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
//                                .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                                .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())

                                .anyRequest()
                                .authenticated()
                )
                // Sessions (making session policy stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                // Authentication (we want to execute jwt auth filter before the username, password auth filter)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Logout
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                // Since are implementing LogoutHandler interface in our service, we do not have to specify the service name here
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;

        return http.build();
    }

}
