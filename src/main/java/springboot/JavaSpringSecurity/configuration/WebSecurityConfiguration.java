package springboot.JavaSpringSecurity.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import springboot.JavaSpringSecurity.security.CustomSuccessHandler;
import springboot.JavaSpringSecurity.security.CustomUserDetailsService;

/**
 * @EnableGlobalMethodSecurity integrate spring security with spring mvc (method security in controllers),
 * we can also use method expressions here (prePostEnabled)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/",
            "/index",
            "/registration**",
            "/login**",
            "/webjars/**",
            "/resources/**"
    };

    private final CustomSuccessHandler customSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;

    /**
     * Security filter chain to set up http config
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Setting cross-origin-resource-sharing
                .cors(Customizer.withDefaults())

                // Whitelisting (endpoints without authentication)
                .csrf(AbstractHttpConfigurer::disable)

                // Authorizing requests
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/admin-page").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user-page").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .anyRequest()
                        .authenticated()
                )

                // Login
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        //.defaultSuccessUrl("/")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("RememberMeCookie")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                /*
                 * Remember me (cookie)
                 * - It is a good practise to request the user to re-authenticate before performing any sensitive
                 *   operation, like changing password, accessing sensitive data, transferring funds, etc.
                 */
                .rememberMe(remember -> remember
                        .tokenValiditySeconds(86400)                    // token validity
                        .rememberMeCookieName("RememberMeCookie")       // setting the name of cookie
                        .tokenRepository(persistentTokenRepository)     // adding persistent token repository
                        .userDetailsService(customUserDetailsService)   // registering our custom service
                )

                /*
                 * Optional Configuration
                 * - The "remember me" feature is typically designed to provide persistent authentication across sessions.
                 * - Here is a basic example of how we can configure session management along with "remember me" cookie.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)   // create a session only if required
                        .sessionFixation().migrateSession()                         // prevent session fixation attacks
                        .maximumSessions(1)                                         // limits the user to only one active session
                        .expiredUrl("/login?expired")                               // redirects to this url on session expiration
                        .maxSessionsPreventsLogin(true)                             // prevents new logins when the maximum number of sessions is reached
                )

        ;

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(customUserDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

}
