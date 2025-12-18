package controller;

import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "AdminOverviewOrderServlet", value = "/AdminOverviewOrderServlet")
public class AdminOverviewOrderServlet extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("LOGGED_USER") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int orderCount = orderDao.getOrderCount(); // OrderDao prints DB value to console
            getServletContext().log("AdminOverviewOrderServlet: orderCount=" + orderCount);
            req.setAttribute("orderCount", orderCount);
            req.getRequestDispatcher("/admin_homepage.jsp").forward(req, resp);
        } catch (Exception e) {
            getServletContext().log("AdminOverviewOrderServlet error", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}