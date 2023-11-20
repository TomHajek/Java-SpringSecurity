package springboot.JavaSpringSecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springboot.JavaSpringSecurity.dto.UserDto;

import springboot.JavaSpringSecurity.service.UserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    /**
     * We can either use dto like this, or use a Model object directly in the get method for empty registration form,
     * to populate the data in model object
     * @return userRegistrationDto
     */
    @ModelAttribute("user")
    public UserDto userRegistrationDto() {
        return new UserDto();
    }

    /**
     * REGISTRATION PAGE - Showing page with an empty registration form
     * @return registration.html
     */
    @GetMapping("/registration")
    public String showUserRegistrationForm() {
        return "registration";
    }

    /**
     * SUBMIT REGISTRATION PAGE - Submitting filled in registration form
     * @param registrationDto passing a user object (with filled user information)
     * @return                redirecting to registration success url, we could also redirect to login page instead
     */
    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") UserDto registrationDto, HttpServletRequest request) {
        userService.createUser(registrationDto);
        return "redirect:/registration?success";
    }

}
