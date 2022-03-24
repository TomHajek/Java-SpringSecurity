package spring.security.exampleJwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.security.exampleDao.service.CustomUserDetailsService;
import spring.security.exampleJwt.filter.JwtFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // using spring security interface which we have to implement
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    //
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // setting config to permit all requests, to be able to go to index or auth page
                .authorizeRequests()
                    .antMatchers("/authenticate")
                    .permitAll()

                // any other requests will be authenticated
                .anyRequest()
                    .authenticated()
                    .and().exceptionHandling()
                    .and().sessionManagement()

                // setting sessions management to be stateless (don't create sessions)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // passing jwt filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    // override method to get user authentication manager bean
    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    // adding bean for our password encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}
