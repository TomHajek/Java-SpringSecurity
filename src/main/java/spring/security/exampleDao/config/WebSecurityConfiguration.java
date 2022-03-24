package spring.security.exampleDao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // using spring security interface which we have to implement
    @Autowired
    UserDetailsService userDetailsService;


    // method to create authentication provider
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());

        return  provider;
    }


    // overriding webSecurityConfigurerAdapter method to configure http security
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // here we are taking authentication from spring and setting it by ourselves
        http.authorizeRequests()
                // allowing default "index" page to all users
                .antMatchers("/")
                .permitAll()

                // setting access for user
                .antMatchers("/home")
                .hasAuthority("USER")

                // setting access for admin
                .antMatchers("/admin")
                .hasAuthority("ADMIN")

                // any request should be authenticated
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

}
