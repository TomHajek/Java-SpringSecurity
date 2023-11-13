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

    /**
     * We can either use dto like this, or use a Model object directly in the get method for empty registration form,
     * to populate the data in model object
     * @return userRegistrationDto
     */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
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
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }

    /**
     * LOGIN PAGE - Showing custom login page
     * @return login.html
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * ALTERNATIVE LOGIN HANDLERS
     * This is only for demo purpose as an alternative for CustomSuccessHandler
     * Also, instead of calling these handlers here, we can use them directly in the http config
     */
    @Deprecated
    @PostMapping("/login_success_handler")
    public String loginSuccessHandler() {
        System.out.println("Login success handler...");
        return "index";
    }

    @Deprecated
    @PostMapping("/login_failure_handler")
    public String loginFailureHandler() {
        System.out.println("Login failure handler...");
        return "login_error";
    }

    /**
     * INDEX PAGE - Showing index page
     * @return index.html
     */
    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    /**
     * USER PAGE - Showing user page
     * @return user.html
     */
    //@PreAuthorize("hasAuthority("ROLE_USER")") // Done in http config
    @GetMapping("/user-page")
    public String viewUserPage (Model model, Principal principal) {
        model.addAttribute("user", customUserDetailsService.loadUserByUsername(principal.getName()));
        return "user";
    }

    /**
     * ADMIN PAGE - Showing admin page
     * @return admin.html
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
