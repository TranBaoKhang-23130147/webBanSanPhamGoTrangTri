package controller;

import dao.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/AdminUpdateRoleServlet")
public class AdminUpdateRoleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            int adminId = Integer.parseInt(request.getParameter("id"));
            String newRole = request.getParameter("role");

            UserDao dao = new UserDao();
            boolean success = dao.updateAdminRole(adminId, newRole);

            response.getWriter().print("{\"success\":" + success + "}");

        } catch (Exception e) {
            response.getWriter().print("{\"success\":false}");
        }
    }
}