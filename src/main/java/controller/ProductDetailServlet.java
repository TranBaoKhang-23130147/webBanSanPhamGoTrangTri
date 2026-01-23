package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.util.HashMap; // THÊM DÒNG NÀY
import java.util.List;
import java.util.Map;      // THÊM DÒNG NÀY

@WebServlet(name = "ProductDetailServlet", value = "/detail")
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        // 🔒 Chốt an toàn
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
        // Xử lý thêm vào giỏ hàng hoặc gửi bình luận ở đây
    }
}