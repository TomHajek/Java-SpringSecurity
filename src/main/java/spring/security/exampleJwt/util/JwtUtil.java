package spring.security.exampleJwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *  Here we are writing logic for token generation and validation
 *  These methods are standard (boilerplate) methods
 */


@Service
public class JwtUtil {

    private String secret = "javatechie";


    // method to extract username from the token
    public String extractUsername(String token) {
        // we get encrypted string form which we extract username(subject)
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    // returns bool if token is expired or not
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // method to generate token
    public String generateToken(String username) {
        // we fetch username and based on that username, we create one token
        Map<String, Object> claims = new HashMap<>();  // empty map
        return createToken(claims, username);          // calling create token method with params
    }


    // method to create token
    private String createToken(Map<String, Object> claims, String username) {
        // using Jwt library and builder to create a token
        return Jwts.builder()
                .setClaims(claims)                                           // empty map param
                .setSubject(username)                                        // username param
                .setIssuedAt(new Date(System.currentTimeMillis()))           // creation date of token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // exp date of token
                .signWith(SignatureAlgorithm.HS256, secret).compact();       // setting hash algorithm
    }


    // method to validate token
    public Boolean validateToken(String token, UserDetails userDetails) {    // userDetails(= username + password)
        // calling method to extract username from the token
        final String username = extractUsername(token);

        // validating username and whether token is expired or not
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
