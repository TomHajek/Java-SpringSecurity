package springboot.JavaSpringSecurity.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import springboot.JavaSpringSecurity.dto.UserRegistrationDto;
import springboot.JavaSpringSecurity.entity.User;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);

}
