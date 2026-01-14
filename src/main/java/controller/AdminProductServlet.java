package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class AdminProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao productDAO = new ProductDao();
        List<Product> productList = productDAO.getAllProductsAdmin();

        request.setAttribute("productList", productList);
        request.getRequestDispatcher("admin_products.jsp").forward(request, response);
    }
}

