package spring.security.exampleJwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.security.exampleJwt.util.JwtUtil;
import spring.security.exampleJwt.entity.AuthRequest;
import spring.security.exampleJwt.service.CustomUserDetailsService;


@RestController
public class WelcomeController {

    // authentication manager to authenticate new username and password auth token
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    // welcoming "default" endpoint
    @GetMapping("/")
    public String welcome() {
        return "Welcome to javatechie !!";
    }


    // authentication endpoint
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        // once user will give request with authRequest(username and password)
        try {
            // evaluate (authenticate) username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

        } catch (Exception ex) {
            throw new Exception("Invalid username or password!");
        }

        // if there is no exception, go head and generate token
        return jwtUtil.generateToken(authRequest.getUsername());
    }


//    // alternative
//    @PostMapping("/authenticate")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
//
//        // once user will give request with authRequest(username and password)
//        try {
//            // evaluate (authenticate) username and password
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authRequest.getUsername(),
//                            authRequest.getPassword()
//                    )
//            );
//
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect username or password", e);
//        }
//
//        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new AuthRequest(jwt));
//    }


}
