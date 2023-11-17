package springboot.JavaSpringSecurity.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import springboot.JavaSpringSecurity.entity.ForgotPasswordToken;
import springboot.JavaSpringSecurity.entity.User;
import springboot.JavaSpringSecurity.repository.ForgotPasswordTokenRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordService.class);

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final JavaMailSender javaMailSender;
    private final UserService userService;

    public void useToken(String token, String password) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);
        forgotPasswordToken.setUsed(true);

        userService.resetUserPassword(forgotPasswordToken.getUser(), password);
        forgotPasswordTokenRepository.save(forgotPasswordToken);
    }

    public String checkTokenValidity(String token, Model model) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);

        if (forgotPasswordToken == null) {
            model.addAttribute("error", "Invalid Token");
            return "error-page";
        } else if (forgotPasswordToken.isUsed()) {
            model.addAttribute("error", "The token is already used");
            return "error-page";
        } else if (isTokenExpired(forgotPasswordToken)) {
            model.addAttribute("error", "The token is expired");
            return "error-page";
        } else {
            return "reset-password";
        }
    }

    private boolean isTokenExpired(ForgotPasswordToken forgotPasswordToken) {
        return LocalDateTime.now().isAfter(forgotPasswordToken.getExpirationTime());
    }

    public void sendEmail(String to, String subject, String url) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String emailContent = "<p>Hello</p>"
                + "Click the link the below to reset your password:"
                + "<p><a href=\"" + url + "\">Change My Password</a></p>"
                + "<br>"
                + "Ignore this Email if you did not made the request.";
        helper.setText(emailContent, true);
        helper.setFrom("application_support@gmail.com", "Application Support");
        helper.setSubject(subject);
        helper.setTo(to);
        javaMailSender.send(message);
        LOGGER.info("Email to {} has been sent: {}", to, message);
    }

    public String createTokenUrl(User user) {
        return "http://localhost:8080/reset-password?token=" + createToken(user).getToken();
    }

    private ForgotPasswordToken createToken(User user) {
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setToken(generateToken());
        forgotPasswordToken.setExpirationTime(expirationTime());
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setUsed(false);
        forgotPasswordTokenRepository.save(forgotPasswordToken);
        return forgotPasswordToken;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime expirationTime() {
        // Setting expiration time range to 10 minutes
        return LocalDateTime.now().plusMinutes(10);
    }

}
