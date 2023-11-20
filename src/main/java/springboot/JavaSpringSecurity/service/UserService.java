package springboot.JavaSpringSecurity.service;

import springboot.JavaSpringSecurity.dto.UserDto;
import springboot.JavaSpringSecurity.entity.User;

import java.util.List;

public interface UserService {

    // FETCH
    List<User> listUsers();
    User getUserByEmail(String email);

    // CREATE, UPDATE
    void createUser(UserDto registrationDto);
    boolean updateUser(String username, UserDto userDto) throws Exception;
    void resetUserPassword(User user, String password);

}
