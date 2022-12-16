package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class AuthTokenVerifier {

    private JWTVerifier verifier;

    public AuthTokenVerifier(String publicKeyPath) {
        Algorithm alg = Algorithm.RSA256(createPublicKey(publicKeyPath));
        this.verifier = JWT.require(alg)
                .withIssuer("AuthTokenProducer")
                .acceptExpiresAt(5)
                .build();
    }

    public DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("JWT verification failed..");
            throw e;
        }
    }

    private RSAPublicKey createPublicKey(String publicKeyPath) {
        try (InputStream is = this.getClass().getResourceAsStream(publicKeyPath);
                BufferedReader buff = new BufferedReader(new InputStreamReader(is))) {
            var pem = new StringBuilder();
            String line;
            while ((line = buff.readLine()) != null) {
                pem.append(line);
            }

            String publicKeyPem = pem.toString()
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "");

                  byte[] encoded = Base64.getDecoder().decode(publicKeyPem);
                  X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
                  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                  return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }
}
