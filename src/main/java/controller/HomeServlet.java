// roi
package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeServlet", value = "/HomeServlet")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null)
                ? (User) session.getAttribute("LOGGED_USER")
                : null;

        ProductDao dao = new ProductDao();
        List<Product> bestSellers = dao.getTop8BestSellers();
        List<Product> products = dao.getProductsByPage(1, 8);

        List<Product> top3Products = dao.getTop3FeaturedProducts();

        request.setAttribute("products", products);
        request.setAttribute("bestSeller", bestSellers);
        request.setAttribute("top3Products", top3Products);

        request.getRequestDispatcher("/homepage_user.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}