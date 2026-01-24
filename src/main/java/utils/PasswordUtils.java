package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // 1. Hàm băm mật khẩu (Dùng khi Đăng ký)
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    // 2. Hàm kiểm tra mật khẩu (Dùng khi Đăng nhập)
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}