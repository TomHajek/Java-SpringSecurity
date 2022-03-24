package spring.security.exampleDao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.security.exampleDao.entity.CustomUserDetails;
import spring.security.exampleDao.entity.User;
import spring.security.exampleDao.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    // overriding method from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // fetching user from db by username
        User user = userRepository.findByUsername(username);

        // checking if user "exist"
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new CustomUserDetails(user);
    }



}
