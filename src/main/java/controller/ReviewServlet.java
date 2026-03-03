package controller;

import dao.ReviewDao;
import model.Reviews;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // 1. Check login
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // 2. Get parameters
            String productIdRaw = request.getParameter("productId");
            String ratingRaw = request.getParameter("rating");
            String comment = request.getParameter("comment");

            System.out.println("=== ReviewServlet ===");
            System.out.println("productId: " + productIdRaw);
            System.out.println("rating: " + ratingRaw);
            System.out.println("comment: " + comment);

            // 3. Validate productId
            if (productIdRaw == null || productIdRaw.isBlank()) {
                session.setAttribute("errorMessage", "Sản phẩm không hợp lệ!");
                response.sendRedirect("homepage_user.jsp");
                return;
            }

            int productId = Integer.parseInt(productIdRaw);

            // 4. Validate rating
            if (ratingRaw == null || ratingRaw.isBlank()) {
                session.setAttribute("errorMessage", "Bạn chưa chọn số sao!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            int rating = Integer.parseInt(ratingRaw);

            if (rating < 1 || rating > 5) {
                session.setAttribute("errorMessage", "Rating phải từ 1 đến 5!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            // 5. Validate comment
            if (comment == null || comment.trim().length() < 10) {
                session.setAttribute("errorMessage", "Bình luận phải ít nhất 10 ký tự!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            if (comment.length() > 500) {
                session.setAttribute("errorMessage", "Bình luận tối đa 500 ký tự!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            // 6. Create review object
            Reviews review = new Reviews();
            review.setUserId(user.getId());
            review.setProductId(productId);
            review.setRating(rating);
            review.setComment(comment.trim());

            ReviewDao dao = new ReviewDao();

            // 7. Check user already reviewed
            if (dao.hasUserReviewed(user.getId(), productId)) {
                session.setAttribute("errorMessage", "Bạn đã đánh giá sản phẩm này rồi!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            // 8. Insert review
            boolean success = dao.addReview(review);

            if (success) {
                session.setAttribute("successMessage", "Cảm ơn bạn đã đánh giá sản phẩm ❤️");
            } else {
                session.setAttribute("errorMessage", "Không thể lưu đánh giá!");
            }

            response.sendRedirect("detail?id=" + productId);

        } catch (NumberFormatException e) {

            e.printStackTrace();
            session.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
            response.sendRedirect("homepage_user.jsp");

        } catch (Exception e) {

            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi hệ thống!");
            response.sendRedirect("homepage_user.jsp");
        }
    }
}
