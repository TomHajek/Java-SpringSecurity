package springboot.JavaSpringSecurity.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springboot.JavaSpringSecurity.entity.User;
import springboot.JavaSpringSecurity.service.ForgotPasswordService;
import springboot.JavaSpringSecurity.service.UserService;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;
    private final UserService userService;

    /**
     * PASSWORD RESET REQUEST FORM - empty form with email field
     * @return password-request.html
     */
    @GetMapping("/password-request")
    public String passwordRequest() {
        return "password-request";
    }

    /**
     * SUBMIT PASSWORD REQUEST RESET FORM - submit request for password reset (send email with token to user)
     * @return redirect to password-request url
     */
    @PostMapping("/password-request")
    public String savePasswordRequest(@RequestParam("username") String username, Model model) {
        // Fetch user by email
        User user = userService.getUserByEmail(username);

        if (user == null) {
            model.addAttribute("error", "This Email is not registered.");
            return "password-request";
        }

        // Create url with reset token
        String url = forgotPasswordService.createTokenUrl(user);

        // Send email to the user
        try {
            forgotPasswordService.sendEmail(user.getEmail(), "Password Reset Link", url);
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
            return "password-request";
        }

        return "redirect:/password-request?success";
    }

    /**
     * PASSWORD RESET FORM - an empty form to set up a new password
     * @return reset-password.html
     */
    @GetMapping("/reset-password")
    public String resetPassword(@Param(value="token") String token, HttpSession session, Model model) {
        session.setAttribute("token", token);
        return forgotPasswordService.checkTokenValidity(token, model);
    }

    /**
     * SUBMIT A PASSWORD RESET FORM - submit form to set up a new password
     * @return reset-password.html
     */
    @PostMapping("/reset-password")
    public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model) {
        String password = request.getParameter("password");
        String token = (String) session.getAttribute("token");

        // Use token and update user password
        forgotPasswordService.useToken(token, password);

        model.addAttribute("message", "You have successfully reset your password");
        return "reset-password";
    }

}
