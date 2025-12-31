package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LivingroomDecorateServlet", value = "/LivingroomDecorateServlet")
public class LivingroomDecorateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao dao = new ProductDao();
        List<Product> listP = dao.getLivingRoomProducts();

        request.setAttribute("listP", listP);
        request.getRequestDispatcher("decorate_livingroom_user.jsp")
                .forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}