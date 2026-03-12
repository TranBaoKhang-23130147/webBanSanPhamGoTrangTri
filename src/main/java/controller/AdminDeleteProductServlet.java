package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/delete-product")
public class AdminDeleteProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        try {
            int productId = Integer.parseInt(idRaw);

            ProductDao productDao = new ProductDao();
            productDao.deleteFullProduct(productId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("admin-products");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}