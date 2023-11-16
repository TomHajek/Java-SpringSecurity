package springboot.JavaSpringSecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.JavaSpringSecurity.dto.UserDto;
import springboot.JavaSpringSecurity.entity.Role;
import springboot.JavaSpringSecurity.entity.User;
import springboot.JavaSpringSecurity.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void saveUser(UserDto registrationDto) {
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRoles(List.of(new Role("ROLE_USER")));
        userRepository.save(user);
    }

    @Override
    public boolean updateUser(String username, UserDto userDto) {
        User userToUpdate = getUserByEmail(username);

        // Do the comparison if login credentials are going to change
        boolean isEmailChanged = userToUpdate.getEmail().equals(userDto.getEmail());
        boolean isPasswordChanged = passwordEncoder.matches(userDto.getPassword(), userToUpdate.getPassword());

        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userToUpdate);

        // If user changed login credentials, log him out in the endpoint
        return isEmailChanged || isPasswordChanged;
    }

}
