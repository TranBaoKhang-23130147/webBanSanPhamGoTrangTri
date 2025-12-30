package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "AdminProfileServlet", value = "/AdminProfileServlet")
public class AdminProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Giả sử Admin ID là 1 (hoặc lấy từ session)
        int adminId = 1;

        UserDao dao = new UserDao();
        User admin = dao.getAdminProfile(adminId);

        // Gửi đối tượng admin sang JSP
        request.setAttribute("admin", admin);
        request.getRequestDispatcher("admin_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}