package spring.security.exampleJwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.security.exampleDao.entity.User;
import spring.security.exampleJwt.repository.UserRepository;

import java.util.ArrayList;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;


    // overriding method from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // fetching user from db by username
        User user = repository.findByUsername(username);

        // checking if user "exist"
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        // returning user from spring security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // getting username from our user entity
                user.getPassword(), // getting password from our user entity
                new ArrayList<>()); // empty arrayList for user roles
    }

}
