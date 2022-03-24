package spring.security.exampleJwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AuthRequest {

    private String username;
    private String password;


    // need default constructor for JSON Parsing

}
