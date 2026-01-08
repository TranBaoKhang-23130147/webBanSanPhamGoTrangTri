package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", value = "/search") // Đổi tên URL cho ngắn gọn
public class SearchController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String txtSearch = request.getParameter("txtSearch");
        String categoryName = request.getParameter("category"); // NAME, KHÔNG PHẢI ID

        if (txtSearch == null) txtSearch = "";
        if (categoryName == null || categoryName.equals("all") || categoryName.equals("null")) {
            categoryName = "all";
        }

        ProductDao dao = new ProductDao();

        Integer categoryId = null;
        if (!categoryName.equals("all")) {
            categoryId = dao.getCategoryIdByName(categoryName); // NAME → ID
        }

        List<Product> list = dao.searchProducts(txtSearch, categoryId);

        request.setAttribute("listP", list);
        request.setAttribute("txtS", txtSearch);
        request.setAttribute("catS", categoryName);

        request.getRequestDispatcher("search.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Cho phép cả phương thức POST cũng chạy như GET
    }
}