package controller;

import dao.ProductDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/admin/update-product-status")
public class AdminUpdateProductStatusServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            int productId = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));

            boolean success = productDao.updateProductStatus(productId, status);

            out.print("{\"success\":" + success + "}");

        } catch (Exception e) {
            response.getWriter().print("{\"success\":false}");
        }
    }
}