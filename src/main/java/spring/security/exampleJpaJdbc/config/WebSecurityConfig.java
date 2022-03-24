package spring.security.exampleJpaJdbc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;


    // adding bean for password encoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    // AUTHORIZATION -> http security
    // setting authorization to various apis based on the user role
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http lets us configure, what are the paths and what are the access restrictions for those paths
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")                       // "/**" = wild card
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/").permitAll()                                 // the least restrictive matcher should be always at the end
                .and().formLogin();
    }


    // AUTHENTICATION -> authentication manager
    // setting configuration on the authentication object (userDetailsService authentication)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


//    // setting configuration on the authentication object (inMemory authentication)
//    // = no need of all the userDetails stuff, but with more config for user
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("blah")
//                .password("blah")   // password should be encoded first
//                .roles("USER")
//                .and()
//                .withUser("foo")
//                .password("foo")
//                .roles("ADMIN");
//    }


//    // only for jdbc authentication
//    @Autowired
//    DataSource dataSource;


//    // setting configuration on the authentication object (jdbc authentication)
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                // this is a schema for default data source (when we have no data in db yet
//                .withDefaultSchema()
//                .withUser(User
//                                .withUsername("user")
//                                .password("1234")
//                                .roles("USER")
//                )
//                .withUser(User
//                                .withUsername("admin")
//                                .password("1234")
//                                .roles("ADMIN")
//                );
//                // we can also use queries, if we have some different db schema
//                .usersByUsernameQuery("select username, password, enabled "
//                        + "from users "
//                        + "where username = ?")
//                .authoritiesByUsernameQuery("select username, authority "
//                        + "from authorities "
//                        + "where username = ?"
//                );
//
//    }

}
