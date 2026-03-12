package controller;

import dao.CategoryDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Category;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductAllServlet", value = "/ProductAllServlet")
public class ProductAllServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDao dao = new ProductDao();
        CategoryDao cDao = new CategoryDao();
        List<Product> list = dao.getAllProducts();
        List<Category> listCC = cDao.getAllCategory();
        request.setAttribute("listCC", listCC);
        request.setAttribute("listType", dao.getAllProductTypes());
        request.setAttribute("listColor", dao.getAllColors());
        request.setAttribute("listP", list);
        request.getRequestDispatcher("product_all_user.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}