package springboot.JavaSpringSecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @EnableGlobalMethodSecurity integrate spring security with spring mvc (method security in controllers),
 * we can also use method expressions here (prePostEnabled)
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Setting cross-origin-resource-sharing
                .cors(Customizer.withDefaults())

                // Whitelisting (endpoints without authentication)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/", "/index","/registration", "/registration**", "/login", "/webjars/**", "/resources/**").permitAll()
                )
        ;


        return http.build();
    }

}
