package springboot.JavaSpringSecurity.service;

import springboot.JavaSpringSecurity.dto.UserDto;
import springboot.JavaSpringSecurity.entity.User;

import java.util.List;

public interface UserService {

    List<User> listUsers();
    User getUserByEmail(String email);
    void saveUser(UserDto registrationDto);
    boolean updateUser(String username, UserDto userDto);

}
