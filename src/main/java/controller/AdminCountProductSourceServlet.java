package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Category;
import model.Source;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminCountProductSourceServlet", value = "/AdminCountProductSourceServlet")
public class AdminCountProductSourceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao productDao = new ProductDao();

        // Danh sách category
        List<Source> listS = productDao.getAllSources();

        // Map<categoryId, soLuongSP>
        Map<Integer, Integer> productCountMap = productDao.countProductBySource();

        request.setAttribute("listS", listS);
        request.setAttribute("productCountMap", productCountMap);
        request.setAttribute("activePage", "source");

        request.getRequestDispatcher("admin_source.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}