package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/AdminChangePasswordController")
public class AdminChangePasswordController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        if (userIdStr == null || userIdStr.equals("")) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect(request.getContextPath() +
                    "/admin/customer-detail?id=" + userIdStr + "&msg=pass_not_match");
            return;
        }
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";
        boolean checkPassword = newPassword.matches(regex);
        if (!checkPassword) {
            response.sendRedirect(request.getContextPath() +
                    "/admin/customer-detail?id=" + userIdStr + "&msg=pass_weak");
            return;
        }
        int userId = Integer.parseInt(userIdStr);
        UserDao userDao = new UserDao();
        boolean result = userDao.updatePasswordById(userId, newPassword);

        if (result) {
            response.sendRedirect(request.getContextPath() +
                    "/admin/customer-detail?id=" + userId + "&msg=pass_success");
        } else {
            response.sendRedirect(request.getContextPath() +
                    "/admin/customer-detail?id=" + userId + "&msg=pass_error");
        }
    }
}