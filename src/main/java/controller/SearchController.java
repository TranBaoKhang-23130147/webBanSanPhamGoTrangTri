package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", value = "/search")
public class SearchController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String txtSearch = request.getParameter("txtSearch");
        if (txtSearch == null) {
            txtSearch = "";
        }
        ProductDao dao = new ProductDao();
        List<Product> list = dao.searchProducts(txtSearch, null);
        request.setAttribute("listP", list);
        request.setAttribute("txtS", txtSearch);
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}