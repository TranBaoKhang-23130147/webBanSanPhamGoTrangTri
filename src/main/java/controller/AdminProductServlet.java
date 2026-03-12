package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class AdminProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String typeId = request.getParameter("typeId");
        String categoryId = request.getParameter("categoryId");

        ProductDao dao = new ProductDao();
        List<Product> list;

        if ((keyword == null || keyword.isEmpty()) &&
                (typeId == null || typeId.isEmpty()) &&
                (categoryId == null || categoryId.isEmpty())) {

            list = dao.getAllProductsAdmin();

        } else {

            list = dao.searchProducts(keyword, typeId, categoryId);
        }

        request.setAttribute("productList", list);
        request.setAttribute("typeList", dao.getAllProductTypes());
        request.setAttribute("categoryList", dao.getAllCategory());

        request.getRequestDispatcher("admin_products.jsp").forward(request, response);
    }
}