package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductAllServlet", value = "/ProductAllServlet")
public class ProductAllServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao dao = new ProductDao();
        List<Product> list = dao.getAllProducts();

        // Dòng này để kiểm tra trên Console của IDE
        System.out.println("DEBUG: So luong san pham lay duoc = " + (list != null ? list.size() : "NULL"));

        request.setAttribute("listP", list);
        request.getRequestDispatcher("product_all_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}