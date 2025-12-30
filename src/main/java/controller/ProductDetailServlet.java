package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", value = "/detail") // Đặt path ngắn gọn cho dễ dùng
public class ProductDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy ID sản phẩm từ URL (ví dụ: detail?id=1)
        String idRaw = request.getParameter("id");


        try {
            if (idRaw != null) {
                int productId = Integer.parseInt(idRaw);
                ProductDao dao = new ProductDao();

                // 2. Lấy thông tin cơ bản + mô tả + thông số (JOIN 3 bảng)
                Product p = dao.getProductById(productId);
                if (p != null) {
                    // Phải gán các danh sách phụ vào p
                    p.setSubImages(dao.getProductImages(productId));
                    p.setVariants(dao.getProductVariants(productId));
                    p.setReviewList(dao.getProductReviews(productId));

                    request.setAttribute("p", p);
                    request.getRequestDispatcher("product_details_user.jsp").forward(request, response);
                } else {
                    // Nếu bị văng về đây, hãy nhìn vào màn hình Console của IntelliJ xem lỗi SQL gì
                    response.sendRedirect("homepage_user.jsp");
                }
            } else {
                response.sendRedirect("homepage_user.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("homepage_user.jsp"); // ID sai định dạng
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thường dùng cho chức năng "Thêm vào giỏ hàng" hoặc "Gửi đánh giá"
    }
}