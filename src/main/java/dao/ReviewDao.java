package dao;

import model.Reviews;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    public boolean addReview(Reviews review) {

        String sql = """
        INSERT INTO reviews(user_id, product_id, rate, comment)
        VALUES (?, ?, ?, ?)
    """;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            int rows = ps.executeUpdate();
            System.out.println("Insert review rows = " + rows);

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy danh sách review của 1 sản phẩm
    public List<Reviews> getReviewsByProductId(int productId) {
        List<Reviews> reviews = new ArrayList<>();
        String query = "SELECT * FROM reviews WHERE product_id = ? ORDER BY createAt DESC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Reviews review = new Reviews();
                review.setId(rs.getInt("id"));
                review.setUserId(rs.getInt("user_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setRating(rs.getInt("rate"));
                review.setComment(rs.getString("comment"));
                review.setCreateAt(rs.getTimestamp("createAt"));
                
                reviews.add(review);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy review: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }
    
    // Kiểm tra user đã đánh giá sản phẩm này chưa
    public boolean hasUserReviewed(int userId, int productId) {

        String sql = """
        SELECT COUNT(*) 
        FROM reviews 
        WHERE user_id = ? AND product_id = ?
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy rating trung bình của sản phẩm
    public double getAverageRating(int productId) {
        String query = "SELECT AVG(CAST(rate AS FLOAT)) as avg_rating FROM reviews WHERE product_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy rating: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Lấy số lượng review của sản phẩm
    public int getReviewCount(int productId) {
        String query = "SELECT COUNT(*) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy số review: " + e.getMessage());
        }
        return 0;
    }
}
