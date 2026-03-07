package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductDetailServlet", value = "/detail")
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect("homepage_user.jsp");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idRaw);
        } catch (NumberFormatException e) {
            response.sendRedirect("homepage_user.jsp");
            return;
        }

        ProductDao dao = new ProductDao();





        Product p = dao.getProductById(productId);

        if (p == null) {
            response.sendRedirect("homepage_user.jsp");
            return;
        }

// 1. Tính tổng số lượng từ các biến thể và gán vào model
        int totalStock = dao.getTotalStockByProductId(productId);
        p.setTotalQuantity(totalStock);

// 2. Load các dữ liệu phụ khác
        p.setSubImages(dao.getProductImages(productId));
        p.setVariants(dao.getProductVariants(productId));



        List<Reviews> reviewList = dao.getProductReviews(productId);
        p.setReviewList(reviewList);
        double avgRating = dao.getAverageRating(productId);
        p.setAverageRating(avgRating);

        Map<Integer, String> userNames = new HashMap<>();
        if (reviewList != null) {
            for (Reviews rev : reviewList) {
                userNames.put(
                        rev.getUserId(),
                        dao.getUsernameById(rev.getUserId())
                );
            }
        }

        request.setAttribute("p", p);
        request.setAttribute("userNames", userNames);

        request.getRequestDispatcher("product_details_user.jsp")
                .forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("addReview".equals(action)) {
            // Xử lý thêm đánh giá
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGGED_USER");
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
            
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = request.getParameter("comment");
                
                // Tạo object Reviews
                Reviews review = new Reviews();
                review.setUserId(user.getId());
                review.setProductId(productId);
                review.setRating(rating);
                review.setComment(comment != null && !comment.trim().isEmpty() ? comment : "");
                
                // Lưu vào database
                ProductDao dao = new ProductDao();
                if (dao.addReview(review)) {
                    session.setAttribute("successMessage", "Cảm ơn bạn đã đánh giá sản phẩm!");
                } else {
                    session.setAttribute("errorMessage", "Không thể thêm đánh giá!");
                }
            } catch (Exception e) {
                session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            }
            
            // Redirect lại trang sản phẩm hiện tại
            response.sendRedirect(request.getContextPath() + "/detail?id=" + request.getParameter("productId"));
        }
    }
}