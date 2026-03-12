package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet("/AdminDeleteCustomerServlet")
public class AdminDeleteCustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentAdmin = null;

        if (session != null) {
            currentAdmin = (User) session.getAttribute("LOGGED_USER");
        }
        if (currentAdmin == null || !currentAdmin.getRole().equals("Admin")) {
            response.sendRedirect("login.jsp");
            return;
        }
        String idRaw = request.getParameter("id");
        String type = request.getParameter("type");

        try {
            int userId = Integer.parseInt(idRaw);
            if (userId != currentAdmin.getId()) {
                UserDao userDao = new UserDao();
                userDao.deleteUser(userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("admin".equalsIgnoreCase(type)) {
            response.sendRedirect("admin-management");
        } else {
            response.sendRedirect("admin-customers");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}