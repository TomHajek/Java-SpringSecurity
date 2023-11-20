package springboot.JavaSpringSecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springboot.JavaSpringSecurity.dto.UserDto;
import springboot.JavaSpringSecurity.entity.User;
import springboot.JavaSpringSecurity.security.CustomUserDetails;
import springboot.JavaSpringSecurity.security.CustomUserDetailsService;
import springboot.JavaSpringSecurity.service.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * @return userDto
     */
    @ModelAttribute("user")
    public UserDto userDto() {
        return new UserDto();
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
    //@PreAuthorize("hasAuthority("ROLE_ADMIN")") // Done in http config
    @GetMapping("/admin-page")
    public String viewAdminPage (Model model, Principal principal) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("userList", userService.listUsers());
        return "admin";
    }

    /**
     * USER ACCOUNT PAGE - Show account information for logged user
     * @param loggedUser CustomUserDetails
     * @param model      User object
     * @return           account.html
     */
    @GetMapping("/account")
    public String viewUserDetails(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model) {
        String email = loggedUser.getUsername();

        // Fetch logged user by "username"
        User user = userService.getUserByEmail(email);

        // Map user to dto
        UserDto userDto = UserDto.of(user);

        // Pass userDto to the thymeleaf
        model.addAttribute("user", userDto);

        if (model.containsAttribute("success")) {
            model.addAttribute("message", "Your account details have been changed!");
        }

        return "account";
    }

    /**
     * SUBMIT USER ACCOUNT PAGE - Update user account details
     * @param loggedUser         CustomUserDetails
     * @param redirectAttributes RedirectAttributes
     * @return                   Redirect to account url
     */
    @PostMapping("/account")
    public String saveUserDetails(@AuthenticationPrincipal CustomUserDetails loggedUser,
                                  @ModelAttribute("user") UserDto userDto,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            // Update user details
            boolean isLoginInfoChanged = userService.updateUser(loggedUser.getUsername(), userDto);

            // Invalidate user session if user credentials are changed
            if (isLoginInfoChanged) {
                invalidateUserSession(request, response);
                return "redirect:/login?logout";
            }

            // We can either use flashAttribute or just addAttribute "message"
            redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");
            return "redirect:/account";
        } catch (Exception exception) {
            //TODO: handle exceptions, for demo purpose I just return to error page
            return "/error-page";
        }
    }

    private void invalidateUserSession(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            logoutHandler.logout(request, response, auth);
        }
    }

}
