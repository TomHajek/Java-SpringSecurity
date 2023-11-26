package springboot.JavaSpringSecurity.utility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * This class serves as a container object for an RSA key
 */
@Getter
@Setter
@Component
public class KeyProperties {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public KeyProperties() {
        KeyPair pair = KeyGenerator.generateRsaKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
    }
    
}
