package controller;

import dao.NotificationDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Notification;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", value = "/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("LOGGED_USER");

        if (user != null && "ADMIN".equals(user.getRole())) {
            NotificationDao notiDAO = new NotificationDao();
            List<Notification> list = notiDAO.getTopNotifications(user.getId());
            request.setAttribute("notifications", list);
        }

        request.getRequestDispatcher("/admin_index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}