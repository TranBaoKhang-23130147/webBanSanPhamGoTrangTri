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

@WebServlet("/admin/product-detail")
public class AdminProductDetailServlet extends HttpServlet {

    private final ProductDao dao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idRaw.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Product p = dao.getProductById(productId);
        if (p == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        // Load dữ liệu phụ - reuse hết hàm cũ
        p.setSubImages(dao.getProductImages(productId));
        p.setVariants(dao.getProductVariants(productId));
        p.setReviewList(dao.getProductReviews(productId));

        // Load tên user cho review
        Map<Integer, String> userNames = new HashMap<>();
        if (p.getReviewList() != null) {
            for (Reviews rev : p.getReviewList()) {
                userNames.put(rev.getUserId(), dao.getUsernameById(rev.getUserId()));
            }
        }

        // TÍNH TOÁN THỐNG KÊ KHO HÀNG CHÍNH XÁC 100%
        // ===== TÍNH TOÁN THỐNG KÊ KHO HÀNG (ĐÚNG NGHIỆP VỤ) =====
        int totalRemaining = 0; // tồn kho hiện tại
        int totalSold = 0;      // đã bán

        if (p.getVariants() != null) {
            for (ProductVariants v : p.getVariants()) {

                // 1. Tồn kho: lấy trực tiếp từ variant
                totalRemaining += v.getInventory_quantity();

                // 2. Đã bán: tổng số lượng trong order (trừ huỷ + hoàn)
                totalSold += dao.getSoldQuantityByVariantId(v.getId());
            }
        }

// 3. Tổng nhập = tồn kho + đã bán
        int totalImported = totalRemaining + totalSold;

// Set lại vào product
        p.setTotalRemaining(totalRemaining);
        p.setTotalSold(totalSold);
        p.setTotalImported(totalImported);


        // Set attribute
        request.setAttribute("p", p);
        request.setAttribute("userNames", userNames);

        // Forward đến đúng trang admin của bạn
        request.getRequestDispatcher("/admin_product_detail.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}