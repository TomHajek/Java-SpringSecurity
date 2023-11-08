package springboot.JavaSpringSecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springboot.JavaSpringSecurity.dto.UserRegistrationDto;
import springboot.JavaSpringSecurity.security.CustomUserDetailsService;
import springboot.JavaSpringSecurity.service.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Showing page with an empty registration form
     */
    @GetMapping("/registration")
    public String showUserRegistrationForm() {
        return "registration";
    }

    /**
     * Submitting the registration form
     *
     * @param registrationDto passing a user object (with filled user information)
     * @return                redirecting to registration success page
     */
    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }

    /**
     * Showing custom login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Showing index page
     */
    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    /**
     * Showing user page
     */
    //@PreAuthorize("hasAuthority("ROLE_USER")") // Done in http config
    @GetMapping("/user-page")
    public String viewUserPage (Model model, Principal principal) {
        model.addAttribute("user", customUserDetailsService.loadUserByUsername(principal.getName()));
        return "user";
    }

    /**
     * Showing admin page
     */
    //@PreAuthorize("hasAuthority("ADMIN")") // Done in http config
    @GetMapping("/admin-page")
    public String viewAdminPage (Model model, Principal principal) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("userList", userService.listUsers());
        return "admin";
    }

}
