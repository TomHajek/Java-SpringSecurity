package springboot.JavaSpringSecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot.JavaSpringSecurity.entity.User;
import springboot.JavaSpringSecurity.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch our user from a database
        User user = userRepository.findByEmail(username).orElseThrow();

        return new CustomUserDetails(user);
    }

}
