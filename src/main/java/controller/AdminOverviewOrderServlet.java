package controller;

import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

@WebServlet("/AdminOverviewOrderServlet")
public class AdminOverviewOrderServlet extends HttpServlet {

    OrderDao orderDao = new OrderDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("LOGGED_USER");
        }

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int orderCount = orderDao.getOrderCount();

        request.setAttribute("orderCount", orderCount);
        request.getRequestDispatcher("admin_homepage.jsp").forward(request, response);
    }
}