package dao;

import model.Order;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDao {
    public List<OrderDetail> getByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = """
        SELECT od.*, p.name, p.image
        FROM order_details od
        JOIN product_variants pv ON od.product_variant_id = pv.id
        JOIN products p ON pv.product_id = p.id
        WHERE od.order_id = ?
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail d = new OrderDetail();
                d.setQuantity(rs.getInt("quantity"));
                d.setTotal(rs.getDouble("total"));
                d.setProductName(rs.getString("name"));
                d.setProductImage(rs.getString("image"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}
