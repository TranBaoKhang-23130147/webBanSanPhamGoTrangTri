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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 12;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        ProductDao dao = new ProductDao();

        List<Product> list = dao.getProductsByPage(page, pageSize);
        int totalProducts = dao.countAllProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        request.setAttribute("listType", dao.getAllProductTypes());
        request.setAttribute("listColor", dao.getAllColors());
        request.setAttribute("listP", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("product_all_user.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}