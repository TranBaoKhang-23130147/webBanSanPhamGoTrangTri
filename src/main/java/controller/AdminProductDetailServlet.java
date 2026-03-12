package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/product-detail")
public class AdminProductDetailServlet extends HttpServlet {

    ProductDao dao = new ProductDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("id"));

        Product p = dao.getProductById(productId);

        p.setSubImages(dao.getProductImages(productId));
        p.setVariants(dao.getProductVariants(productId));
        p.setReviewList(dao.getProductReviews(productId));

        Map<Integer, String> userNames = new HashMap<>();

        if (p.getReviewList() != null) {
            for (Reviews r : p.getReviewList()) {
                userNames.put(r.getUserId(), dao.getUsernameById(r.getUserId()));
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

        p.setTotalRemaining(totalRemaining);
        p.setTotalSold(totalSold);
        p.setTotalImported(totalRemaining + totalSold);

        request.setAttribute("p", p);
        request.setAttribute("userNames", userNames);

        request.getRequestDispatcher("admin_product_detail.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}