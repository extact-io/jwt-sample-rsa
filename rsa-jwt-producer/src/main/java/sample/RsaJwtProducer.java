package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class RsaJwtProducer {

    private String keyPath;

    public RsaJwtProducer(String path) {
        this.keyPath = path;
    }

    public String generateToken() {
        Algorithm alg = Algorithm.RSA256(createPrivateKey());
        String token = JWT.create()
                .withIssuer("RsaJwtProducer")
                .withSubject("ID12345")
                .withExpiresAt(OffsetDateTime.now().plusMinutes(60).toInstant())
                .withIssuedAt(OffsetDateTime.now().toInstant())
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("email", "id123459@exact.io")
                .withArrayClaim("groups", new String[] { "member", "admin" })
                .sign(alg);
        return token;
    }

    private RSAPrivateKey createPrivateKey() {
        try (InputStream is = this.getClass().getResourceAsStream(this.keyPath);
                BufferedReader buff = new BufferedReader(new InputStreamReader(is))) {
            var pem = new StringBuilder();
            String line;
            while ((line = buff.readLine()) != null) {
                pem.append(line);
            }

            String privateKeyPem = pem.toString()
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");

                  byte[] encoded = Base64.getDecoder().decode(privateKeyPem);
                  PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

                  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                  return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        String keyPath = System.getenv("KEY_PATH");
        System.out.println(new RsaJwtProducer(keyPath).generateToken());
    }
}
