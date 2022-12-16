package sample;

import java.util.List;

public class UserAuthenticator {
    private static final List<User> USERS = List.of(
                new User("soramame", "emamaros", "そら豆 太郎"),
                new User("edamame", "emamade", "えだ豆 次郎"),
                new User("kuromame", "emamoruk", "くろ豆 三郎")
            );
    public User authenticate(String id, String password) {
        return USERS.stream()
                .filter(user -> user.id().equals(id))
                .filter(user -> user.password().equals(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unkwon user."));
    }
}
