package EVENT;

public class AuthManager {
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static boolean authenticate(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}
