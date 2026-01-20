package dao;


import model.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SizeDao {
    public List<Size> getAll() {
        List<Size> list = new ArrayList<>();
        String sql = "SELECT id, size_name FROM sizes";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Size(rs.getInt("id"), rs.getString("size_name")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
