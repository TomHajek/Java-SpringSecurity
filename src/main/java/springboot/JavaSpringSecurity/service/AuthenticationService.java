package springboot.JavaSpringSecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.JavaSpringSecurity.dto.LoginDto;
import springboot.JavaSpringSecurity.entity.AppUser;
import springboot.JavaSpringSecurity.entity.Role;
import springboot.JavaSpringSecurity.repository.AppUserRepository;
import springboot.JavaSpringSecurity.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AppUser registerUser(String username, String password){
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        AppUser newUser = new AppUser();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAuthorities(authorities);

        return userRepository.save(newUser);
    }

    public LoginDto loginUser(String username, String password){
        try {
            //Creating a new authentication object to authenticate user and create generic auth token if user is valid
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            //Creating JWT out of auth token
            String token = tokenService.generateJwt(auth);

            //Login response object
            return new LoginDto(userRepository.findByUsername(username).get(), token);

        } catch(AuthenticationException e) {
            return new LoginDto(null, "");
        }
    }

}
