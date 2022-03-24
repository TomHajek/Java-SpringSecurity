package spring.security.examplePersistentToken.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


/**
 * Spring Boot implemented the default Spring Security autoconfiguration in SecurityAutoConfiguration. To switch
 * the default web application security configuration and provide our own customized security configuration, we can
 * create a configuration class that extends WebSecurityConfigurerAdapter and is annotated with @EnableWebSecurity.
 *
 * This example configures CustomUserDetailsService and BCryptPasswordEncoder to be used by AuthenticationManager
 * instead of the default in-memory database with a single-user with a plaintext password.
 *
 * The configure(HttpSecurity http) method is configured to:
 *     * Ignore the static resource paths "/resources/", "/webjars/", and "/assets/**"
 *     * Allow everyone to have access to the root URL "/"
 *     * Restrict access to URLs that start with /admin/ to only users with the ADMIN role
 *     * All other URLs should be accessible to authenticated users only
 *
 * We are also configuring custom form-based login parameters and making them accessible to everyone. The example also
 * configures the URL to redirect the users to the /accessDenied URL if they try to access a resource they don’t have
 * access to. We are going to use Thymeleaf view templates for rendering views. The thymeleaf-extrasspringsecurity4
 * module provides Thymeleaf Spring Security dialect attributes (sec:authentication, sec:authorize, etc.) to
 * conditionally render parts of the view based on authentication status, logged-in user roles, etc.
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private DataSource dataSource;


    // bean for password encryption
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                    .frameOptions().sameOrigin()
                    .and()
                .authorizeRequests()
                    .antMatchers("/resources/**", "/webjars/**","/assets/**").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .failureUrl("/login?error")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .deleteCookies("my-remember-me-cookie")
                    .permitAll()
                    .and()
                .rememberMe()
                    //.key("my-secure-key")
                    .rememberMeCookieName("my-remember-me-cookie")
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(24 * 60 * 60)
                    .and()
                .exceptionHandling();
    }


    PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);

        return tokenRepositoryImpl;
    }

}
