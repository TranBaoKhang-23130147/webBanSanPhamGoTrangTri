package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AdminChangePasswordController", value = "/AdminChangePasswordController")
public class AdminChangePasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdStr = request.getParameter("userId");
        String password = request.getParameter("newPassword");
        String rePassword = request.getParameter("confirmPassword");

        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
            return;
        }

        if (!password.equals(rePassword)) {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_not_match");
            return;
        }

        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(regex)) {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_weak");
            return;
        }

        UserDao dao = new UserDao();
        boolean success = dao.updatePasswordById(Integer.parseInt(userIdStr), password);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_success");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_error");
        }
    }
}