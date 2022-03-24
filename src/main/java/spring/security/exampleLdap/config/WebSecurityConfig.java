package spring.security.exampleLdap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // we are telling spring how are ldap data structured (configuring existing setting)
        auth.ldapAuthentication()
                .userDnPatterns("uid={0}, ou=people")                           // dn(distinguish name), uid(user information),
                .groupSearchBase("ou=groups")                                   // ou(organisation unit)
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework, dc=org")        // server host
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())                   // password encoder
                .passwordAttribute("userPassword");                             // user password
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // fully authorize to any request
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and().
                formLogin();
    }

}
