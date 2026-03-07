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

        p.setSubImages(dao.getProductImages(productId));
        p.setVariants(dao.getProductVariants(productId));
        p.setReviewList(dao.getProductReviews(productId));

        Map<Integer, String> userNames = new HashMap<>();
        if (p.getReviewList() != null) {
            for (Reviews rev : p.getReviewList()) {
                userNames.put(rev.getUserId(), dao.getUsernameById(rev.getUserId()));
            }
        }

        int totalRemaining = 0;
        int totalSold = 0;
        if (p.getVariants() != null) {
            for (ProductVariants v : p.getVariants()) {

                totalRemaining += v.getInventory_quantity();

                totalSold += dao.getSoldQuantityByVariantId(v.getId());
            }
        }

        int totalImported = totalRemaining + totalSold;

        p.setTotalRemaining(totalRemaining);
        p.setTotalSold(totalSold);
        p.setTotalImported(totalImported);


        request.setAttribute("p", p);
        request.setAttribute("userNames", userNames);

        request.getRequestDispatcher("/admin_product_detail.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}