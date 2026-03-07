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
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.setAttribute("admin", loggedUser);
        request.getRequestDispatcher("/admin_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        int currentAdminId = loggedUser.getId();

        try {
            String avatarUrlRaw = request.getParameter("adminAvatarUrl");

            String relativeAvatarUrl = null;
            if (avatarUrlRaw != null && !avatarUrlRaw.trim().isEmpty()) {
                relativeAvatarUrl = avatarUrlRaw.trim();
                String contextPath = request.getContextPath();

                if (relativeAvatarUrl.startsWith(contextPath)) {
                    relativeAvatarUrl = relativeAvatarUrl.substring(contextPath.length());
                }
            }

            if (relativeAvatarUrl != null) {
                int imageId = userDao.getImageIdByUrl(relativeAvatarUrl);

                boolean success = userDao.updateUserAvatarId(currentAdminId, imageId);

                if (success) {
                    loggedUser.setAvatarId(imageId);
                    loggedUser.setAvatarUrl(relativeAvatarUrl);
                    session.setAttribute("LOGGED_USER", loggedUser);

                    response.sendRedirect("AdminProfileServlet?status=success");
                } else {
                    response.sendRedirect("AdminProfileServlet?status=error");
                }
            } else {
                response.sendRedirect("AdminProfileServlet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminProfileServlet?status=exception");
        }
    }
}