package dao;

import java.sql.*;

public class ContactDao {
    public int insertContactReturnId(Integer userId, String fname, String lname,
                                     String email, String phone, String content) {

        String sql = """
            INSERT INTO contacts(user_id, fname, lname, email, phone, content)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (userId != null)
                ps.setInt(1, userId);
            else
                ps.setNull(1, Types.INTEGER);

            ps.setString(2, fname);
            ps.setString(3, lname);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, content);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
