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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {

            String productIdStr = request.getParameter("productId");
            String ratingStr = request.getParameter("rating");
            String comment = request.getParameter("comment");

            if (productIdStr == null || productIdStr.equals("")) {
                session.setAttribute("errorMessage", "Sản phẩm không hợp lệ!");
                response.sendRedirect("homepage_user.jsp");
                return;
            }

            int productId = Integer.parseInt(productIdStr);

            if (ratingStr == null || ratingStr.equals("")) {
                session.setAttribute("errorMessage", "Bạn chưa chọn số sao!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            int rating = Integer.parseInt(ratingStr);

            if (rating < 1 || rating > 5) {
                session.setAttribute("errorMessage", "Rating phải từ 1 đến 5!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

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

            Reviews review = new Reviews();

            review.setUserId(user.getId());
            review.setProductId(productId);
            review.setRating(rating);
            review.setComment(comment.trim());

            ReviewDao dao = new ReviewDao();

            if (dao.hasUserReviewed(user.getId(), productId)) {
                session.setAttribute("errorMessage", "Bạn đã đánh giá sản phẩm này rồi!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            boolean result = dao.addReview(review);

            if (result) {
                session.setAttribute("successMessage", "Cảm ơn bạn đã đánh giá sản phẩm");
            } else {
                session.setAttribute("errorMessage", "Không thể lưu đánh giá!");
            }

            response.sendRedirect("detail?id=" + productId);

        } catch (Exception e) {

            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi hệ thống!");
            response.sendRedirect("homepage_user.jsp");
        }
    }
}