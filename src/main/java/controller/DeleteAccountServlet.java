package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "DeleteAccountServlet", value = "/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGGED_USER") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("LOGGED_USER");

        try {
            UserDao dao = new UserDao();
            boolean success = dao.deleteUser(user.getId());

            if (success) {
                session.invalidate(); // 👈 logout luôn
                response.sendRedirect("login.jsp?msg=account_deleted");
            } else {
                response.sendRedirect("admin_setting.jsp?error=delete_failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin_setting.jsp?error=server_error");
        }
    }
}