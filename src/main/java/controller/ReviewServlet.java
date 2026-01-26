package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Reviews;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("MyPageServlet?tab=don-hang");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        // ===== CHECK LOGIN =====
        User user = (User) session.getAttribute("LOGGED_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // ===== GET ORDER ID =====
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            response.sendRedirect("MyPageServlet?tab=don-hang");
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);

        UserDao dao = new UserDao();

        // ===== LẤY PRODUCT UNIQUE =====
        List<Reviews> products = dao.getUniqueProductsToReview(orderId);

        boolean hasError = false;

        for (Reviews p : products) {

            int productId = p.getProductId();

            String ratingStr = request.getParameter("rating_" + productId);
            String comment = request.getParameter("comment_" + productId);

            // Nếu user không đánh thì bỏ qua
            if (ratingStr == null || ratingStr.isEmpty()) continue;

            Reviews r = new Reviews();
            r.setUserId(user.getId());
            r.setProductId(productId);
            r.setRating(Integer.parseInt(ratingStr));
            r.setComment(comment != null ? comment.trim() : "");

            if (!dao.insertReview(r)) {
                hasError = true;
            }
        }

        // ===== UPDATE ORDER STATUS =====
        if (!hasError) {
            dao.updateOrderRatedStatus(orderId, true);
            session.setAttribute("msg", "Đánh giá thành công!");
        } else {
            session.setAttribute("msg", "Có lỗi khi lưu đánh giá.");
        }

        response.sendRedirect("MyPageServlet?tab=don-hang");
    }
}
