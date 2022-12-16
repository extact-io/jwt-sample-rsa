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
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class RsaJwtConsumer {

    private String publicKeyPath;

    public RsaJwtConsumer(String path) {
        this.publicKeyPath = path;
    }

    public DecodedJWT verifyToken(String token) {
        Algorithm alg = Algorithm.RSA256(createPublicKey());
        JWTVerifier verifier = JWT.require(alg)
                .withIssuer("RsaJwtProducer")
                .acceptExpiresAt(5)
                .build();
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("JWT verification failed..");
            throw e;
        }
    }

    private RSAPublicKey createPublicKey() {
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

    public static void main(String[] args) {
        String secretkey = System.getenv("PUB_KEY_PATH");

        DecodedJWT jwt = new RsaJwtConsumer(secretkey).verifyToken(args[0]);

        System.out.println("----- DecodedJWT -----");
        System.out.println("alg:" + jwt.getAlgorithm());
        System.out.println("typ:" + jwt.getType());
        System.out.println("issuer:" + jwt.getIssuer());
        System.out.println("subject:" + jwt.getSubject());
        System.out.println("expiresAt:" + jwt.getExpiresAt());
        System.out.println("issuerAt:" + jwt.getIssuedAt());
        System.out.println("JWT-ID:" + jwt.getId());
        System.out.println("email:" + jwt.getClaim("email").asString());
        System.out.println("groups:" + jwt.getClaim("groups")
                    .asList(String.class).stream()
                    .collect(Collectors.joining(",")));
    }
}
