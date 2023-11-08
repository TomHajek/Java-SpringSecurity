package springboot.JavaSpringSecurity.service;

import springboot.JavaSpringSecurity.dto.UserRegistrationDto;
import springboot.JavaSpringSecurity.entity.User;

import java.util.List;

public interface UserService {

    User save(UserRegistrationDto registrationDto);
    List<User> listUsers();

}
