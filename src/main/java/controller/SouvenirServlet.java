package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SouvenirServlet", value = "/SouvenirServlet")
public class SouvenirServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String categoryKey = request.getParameter("category"); // phong-khach
        int page = 1;
        int pageSize = 12;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        ProductDao dao = new ProductDao();
        Integer categoryId = dao.getCategoryIdByName(categoryKey);

        List<Product> listP;
        int totalProducts;

        if (categoryId == null) {
            listP = dao.getProductsByPage(page, pageSize);
            totalProducts = dao.countAllProducts();
        } else {
            listP = dao.getProductsByCategoryPaging(categoryId, page, pageSize);
            totalProducts = dao.countProductsByCategory(categoryId);
        }

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        request.setAttribute("listP", listP);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("category", categoryKey);

        request.getRequestDispatcher("souvenirs_user.jsp")
                .forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}