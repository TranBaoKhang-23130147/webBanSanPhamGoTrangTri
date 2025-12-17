package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// Hãy đảm bảo bạn đã có lớp DBContext để lấy Connection
import dao.DBContext;

public class CategoryDao {
    public boolean insertCategory(String categoryName) {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";

        // Thay vì: try (Connection conn = new DBContext().getConnection();
// Hãy sửa thành:
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Thêm dòng này để kiểm tra chính xác Database đang ghi vào
            System.out.println("Ghi dữ liệu vào DB: " + conn.getMetaData().getURL());

            ps.setString(1, categoryName);
            int check = ps.executeUpdate();
            return check > 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}