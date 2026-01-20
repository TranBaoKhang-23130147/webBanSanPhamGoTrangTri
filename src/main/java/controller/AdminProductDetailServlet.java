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
        int totalImported = 0;
        int totalSold = 0;

        if (p.getVariants() != null) {
            for (ProductVariants v : p.getVariants()) {
                totalImported += v.getInventory_quantity();
                totalSold += dao.getSoldQuantityByVariantId(v.getId()); // ĐÂY LÀ CHỖ QUAN TRỌNG
            }
        }

        p.setTotalImported(totalImported);
        p.setTotalSold(totalSold);
        p.setTotalRemaining(totalImported - totalSold);

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