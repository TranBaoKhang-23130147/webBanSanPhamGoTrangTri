package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MiniitemDecorateServlet", value = "/MiniitemDecorateServlet")
public class MiniitemDecorateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryKey = request.getParameter("category");

        ProductDao dao = new ProductDao();

        // 🔥 2. ĐỔI NAME → ID
        Integer categoryId = dao.getCategoryIdByName(categoryKey);

        List<Product> listP;

        if (categoryId == null) {
            listP = dao.getAllProducts(); // fallback HIỂN THỊ ĐƯỢC
        } else {
            listP = dao.getProductsByCategory(categoryId);
        }

        request.setAttribute("listP", listP);
        request.getRequestDispatcher("decorate_miniitem_user.jsp")
                .forward(request, response);
        System.out.println("categoryKey = " + categoryKey);
        System.out.println("categoryId = " + categoryId);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}