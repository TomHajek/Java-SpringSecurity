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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
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
