package dao;


import model.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SizeDao {
    public SizeDao() throws Exception {
    }

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
    Connection conn = new DBContext().getConnection();



    public void save(Size s) {
        String sql = s.getId() == 0 ?
                "insert into sizes(size_name) values(?)"
                :
                "update sizes set size_name=? where id=?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getSizeName());
            if (s.getId() != 0)
                ps.setInt(2, s.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void delete(int id) {

        String sql = "DELETE FROM sizes WHERE id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
