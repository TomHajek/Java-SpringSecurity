package springboot.JavaSpringSecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springboot.JavaSpringSecurity.dto.LoginDto;
import springboot.JavaSpringSecurity.dto.RegistrationDto;
import springboot.JavaSpringSecurity.entity.AppUser;
import springboot.JavaSpringSecurity.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody RegistrationDto body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginDto loginUser(@RequestBody RegistrationDto body) {
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

}
