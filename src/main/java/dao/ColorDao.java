package dao;

import model.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ColorDao {
    public List<Color> getAll() {
        List<Color> list = new ArrayList<>();
        String sql = "SELECT id, colorname, color_code FROM colors";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Color(rs.getInt("id"), rs.getString("colorname"), rs.getString("color_code")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
