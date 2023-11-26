package springboot.JavaSpringSecurity.utility;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Utility class with static method to generate an RSA key (KeyPair, containing public and private keys)
 */
public class KeyGenerator {

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch(Exception e) {
            throw new IllegalStateException();
        }

        return keyPair;
    }

}
