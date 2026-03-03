package dao;

import model.Color;
import java.sql.*;
import java.util.*;

public class ColorDao {

    // ================= GET ALL =================
    public List<Color> getAll() {

        List<Color> list = new ArrayList<>();

        String sql = "SELECT * FROM colors";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Color(
                        rs.getInt("id"),
                        rs.getString("colorname"),
                        rs.getString("color_code")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= SAVE / UPDATE =================
    public void save(Color c) {

        String sql = c.getId() == 0
                ? "INSERT INTO colors(colorname,color_code) VALUES(?,?)"
                : "UPDATE colors SET colorname=?, color_code=? WHERE id=?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getColorName());
            ps.setString(2, c.getColorCode());

            if (c.getId() != 0)
                ps.setInt(3, c.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    public void delete(int id) {

        String sql = "DELETE FROM colors WHERE id=?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET BY ID (OPTIONAL) =================
    public Color getById(int id) {

        String sql = "SELECT * FROM colors WHERE id=?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Color(
                        rs.getInt("id"),
                        rs.getString("colorname"),
                        rs.getString("color_code")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
