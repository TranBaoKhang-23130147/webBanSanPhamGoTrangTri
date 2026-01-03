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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        try {
            if (idRaw != null) {
                int productId = Integer.parseInt(idRaw);
                ProductDao dao = new ProductDao();

                Product p = dao.getProductById(productId);

                if (p != null) {
                    p.setSubImages(dao.getProductImages(productId));
                    p.setVariants(dao.getProductVariants(productId));

                    // 1. Lấy danh sách đánh giá
                    List<Reviews> reviewList = dao.getProductReviews(productId);
                    p.setReviewList(reviewList);

                    // 2. TẠO MAP ĐỂ CHỨA TÊN NGƯỜI DÙNG (Key: userId, Value: username)
                    Map<Integer, String> userNames = new HashMap<>();
                    if (reviewList != null) {
                        for (Reviews rev : reviewList) {
                            // Gọi hàm lấy tên từ DAO dựa vào ID (không cần sửa Model)
                            String name = dao.getUsernameById(rev.getUserId());
                            userNames.put(rev.getUserId(), name);
                        }
                    }

                    // 3. Gửi cả Product và Map tên sang JSP
                    request.setAttribute("p", p);
                    request.setAttribute("userNames", userNames); // Gửi Map này đi

                    request.getRequestDispatcher("product_details_user.jsp").forward(request, response);
                } else {
                    response.sendRedirect("homepage_user.jsp");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để dễ kiểm tra
            response.sendRedirect("homepage_user.jsp");
        }
    }

    // ... doPost giữ nguyên ...

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý thêm vào giỏ hàng hoặc gửi bình luận ở đây
    }
}