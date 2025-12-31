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
                // Giả sử productId lấy từ request.getParameter("id")
                Product p = dao.getProductById(productId);

                if (p != null) {
                    // GỌI CÁC HÀM NÀY ĐỂ ĐỔ DỮ LIỆU VÀO LIST
                    List<Images> subImages = dao.getProductImages(productId);
                    List<ProductVariants> variants = dao.getProductVariants(productId);
                    List<Reviews> reviews = dao.getProductReviews(productId);

                    // Gán ngược lại vào đối tượng p
                    p.setSubImages(subImages);
                    p.setVariants(variants);
                    p.setReviewList(reviews); // Biến này khớp với c:forEach items="${p.reviewList}" ở JSP

                    // Tính toán sơ bộ tổng số review và trung bình sao nếu cần
                    p.setTotalReviews(reviews.size());

                    request.setAttribute("p", p);
                    request.getRequestDispatcher("product_details_user.jsp").forward(request, response);
                } else {
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