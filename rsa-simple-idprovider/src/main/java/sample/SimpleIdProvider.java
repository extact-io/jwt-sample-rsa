package sample;

public class SimpleIdProvider {

    private UserAuthenticator authenticator;
    private AuthTokenProducer jwtProducer;

    public SimpleIdProvider(UserAuthenticator authenticator, AuthTokenProducer jwtProducer) {
        this.authenticator = authenticator;
        this.jwtProducer = jwtProducer;
    }

    public String publishToken(String id, String password) {
        User authUser = authenticator.authenticate(id, password);
        return jwtProducer.generateToken(authUser);
    }

    public static void main(String[] args) {
        String keyPath = System.getenv("KEY_PATH");
        String id = args[0];
        String password = args[1];

        SimpleIdProvider idProvider = new SimpleIdProvider(
                new UserAuthenticator(),
                new AuthTokenProducer(keyPath));

        String token = idProvider.publishToken(id, password);

        System.out.println(token);
    }
}
