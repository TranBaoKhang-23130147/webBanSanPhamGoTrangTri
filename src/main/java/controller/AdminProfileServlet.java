package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

@WebServlet("/AdminProfileServlet")
public class AdminProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("admin", user);
        request.getRequestDispatcher("admin_profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String avatarUrl = request.getParameter("adminAvatarUrl");

        try {
            UserDao dao = new UserDao();

            int imageId = dao.getImageIdByUrl(avatarUrl);

            dao.updateUserAvatarId(user.getId(), imageId);

            user.setAvatarId(imageId);
            user.setAvatarUrl(avatarUrl);

            session.setAttribute("LOGGED_USER", user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("admin-profile");
    }
}